package faang.school.accountservice.service.savings;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.accountservice.model.SavingsAccount;
import faang.school.accountservice.model.Tariff;
import faang.school.accountservice.repository.SavingsAccountRepository;
import faang.school.accountservice.repository.TariffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccrualToScore {
    private final JdbcTemplate template;
    private final ObjectMapper objectMapper;
    private final ExecutorService requestPool;
    private final ExecutorService accrualExecutor;
    private final SavingsAccountRepository savingsAccountRepository;
    private final TariffRepository tariffRepository;
    private final String UPDATE_ACCOUNTS_SQL = """
            update savings_account set amount = ?, time_accrual = ? where id = ?;
            """;
    @Value("${accrual.size-select-db}")
    private int requestSize;
    @Value("${accrual.accrual-run-size}")
    private int packetSizeForStream;
    private LocalDateTime checkTime;
    private Map<Integer, Integer> mapThread;
    AtomicInteger repeatCheck;

    @Scheduled(cron = "0 0 6 * * *")
    public void accrualPercent() {
        mapThread = new ConcurrentHashMap<>();
        repeatCheck = new AtomicInteger(0);
        checkTime = LocalDateTime.now().minus(24, TimeUnit.HOURS.toChronoUnit());
        LocalDateTime newDateTime = LocalDateTime.now();
        List<Long> ids = savingsAccountRepository.activeAccountsId();
        List<Tariff> tariffs = tariffRepository.findAll();
        Map<Long, Float> map = new HashMap<>(tariffs.size());
        for (Tariff tariff : tariffs) {
            map.put(tariff.getId(), tariff.getBet());
        }

        if (ids.size() <= requestSize) {
            accrual(getAccount(ids), map, newDateTime);
            return;
        }

        for (List<Long> list : subList(ids, requestSize)) {
            requestPool.submit(() -> accrual(getAccount(list), map, newDateTime));
        }
    }

    private List<SavingsAccount> getAccount(List<Long> listId) {
        return checkTime(savingsAccountRepository.findAllById(listId));
    }

    private List<SavingsAccount> checkTime(List<SavingsAccount> accounts) {
        return accounts.stream().filter(time -> time.getLastTimeOfAccrual().isBefore(checkTime)).toList();
    }

    public void run(List<SavingsAccount> accounts, Map<Long, Float> map, LocalDateTime newTime, int check) {
        List<SavingsAccount> checkAccounts = null;
        if (mapThread.containsKey(check)) {
            mapThread.put(check, mapThread.get(check) + 1);
        } else {
            mapThread.put(check, 0);
            checkAccounts = checkTime(accounts);
            if (checkAccounts.isEmpty()) {
                mapThread.remove(check);
                return;
            }
        }
        try {
            template.batchUpdate(UPDATE_ACCOUNTS_SQL, checkAccounts, accounts.size(),
                    (PreparedStatement ps, SavingsAccount account) -> {
                        List<Long> tariffs;
                        try {
                            tariffs = objectMapper.readValue(account.getTariffHistory(), new TypeReference<>() {
                            });
                        } catch (JsonProcessingException e) {
                            log.error(e.toString());
                            return;
                        }

                        ps.setBigDecimal(1, calculatePercentages(account, map, tariffs).setScale(5, RoundingMode.DOWN));
                        ps.setObject(2, newTime);
                        ps.setLong(3, account.getId());
                    });

        } catch (DataAccessException dae) {
            if (dae.getCause() instanceof BatchUpdateException bue) {
                int[] updateCounts = bue.getUpdateCounts();
                List<SavingsAccount> repeat = accounts.subList(updateCounts.length, accounts.size());
                if (mapThread.get(check) >= 3) {
                    mapThread.remove(check);
                    log.error(repeat.toString());
                    return;
                }
                run(repeat, map, newTime, check);
            }
        }
        mapThread.remove(check);
    }

    private BigDecimal calculatePercentages(SavingsAccount account, Map<Long, Float> map, List<Long> tariffs) {
        BigDecimal amount = account.getAmount();
        BigDecimal percent;
        float bet = map.get(tariffs.get(tariffs.size() - 1));
        percent = amount.multiply(BigDecimal.valueOf(bet));
        percent = percent.divide(BigDecimal.valueOf(100), 5, RoundingMode.DOWN);
        percent = percent.divide(BigDecimal.valueOf(365), 5, RoundingMode.DOWN);
        amount = amount.add(percent);
        return amount;
    }

    private void accrual(List<SavingsAccount> list, Map<Long, Float> map, LocalDateTime newTime) {
        if (list.size() <= packetSizeForStream) {
            run(list, map, newTime, repeatCheck.incrementAndGet());
        } else {
            for (List<SavingsAccount> subs : subList(list, packetSizeForStream)) {
                accrualExecutor.submit(() -> run(subs, map, newTime, repeatCheck.incrementAndGet()));
            }
        }
    }

    private <T> List<List<T>> subList(List<T> list, int range) {
        int from = 0;
        int to = range;
        int sizeList = list.size() / to;

        if (list.size() % to != 0) {
            sizeList++;
        }

        List<List<T>> subLists = new ArrayList<>(sizeList);
        for (int i = 0; i < sizeList - 1; i++) {
            subLists.add(list.subList(from, to));
            from += range;
            to += range;
        }
        subLists.add(list.subList(from, list.size()));
        return subLists;
    }
}
