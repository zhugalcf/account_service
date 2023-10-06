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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccrualToScore {
    private final ObjectMapper objectMapper;
    private final ExecutorService requestPool;
    private final ExecutorService accrualExecutor;
    private final SavingsAccountRepository savingsAccountRepository;
    private final TariffRepository tariffRepository;
    private final AtomicInteger counter = new AtomicInteger(0);
    private final String UPDATE_ACCOUNTS_SQL = """
            update savings_account set amount = ?, time_accrual = ? where id = ?;
            """;
    @Value("${spring.datasource.url}")
    private String URL;
    @Value("${spring.datasource.username}")
    private String USER;
    @Value("${spring.datasource.password}")
    private String PASSWORD;
    @Value("${accrual.size-select-db}")
    private int requestSize;
    @Value("${accrual.accrual-run-size}")
    private int packetSizeForStream;

    @Scheduled(cron = "0 0 6 * * *")
    public void accrualPercent() {
        counter.set(0);
        LocalDateTime newDateTime = LocalDateTime.now();
        LocalDateTime checkDateTime = LocalDateTime.now().minus(24, TimeUnit.HOURS.toChronoUnit());
        List<Long> ids = savingsAccountRepository.activeAccountsId();

        Function<List<SavingsAccount>, List<SavingsAccount>> checkTime = (lists) -> lists.stream()
                .filter(time -> time.getLastTimeOfAccrual().isBefore(checkDateTime)).toList();

        List<Tariff> tariffs = tariffRepository.findAll();
        Map<Long, Float> map = new HashMap<>(tariffs.size());
        for (Tariff tariff : tariffs) {
            map.put(tariff.getId(), tariff.getBet());
        }

        if (ids.size() <= requestSize) {
            accrual(getAccount(ids), map, checkTime, newDateTime, ids);
            return;
        }

        for (List<Long> list : subList(ids, requestSize)) {
            requestPool.submit(() -> accrual(getAccount(list), map, checkTime, newDateTime, list));
        }
    }

    private List<SavingsAccount> getAccount(List<Long> listId) {
        return savingsAccountRepository.findAllById(listId);
    }

    public void run(List<SavingsAccount> accounts, Map<Long, Float> map, LocalDateTime newTime) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = conn.prepareStatement(UPDATE_ACCOUNTS_SQL)) {
            int count = 0;
            for (SavingsAccount account : accounts) {
                List<Long> tariffs;
                try {
                    tariffs = objectMapper.readValue(account.getTariffHistory(), new TypeReference<>() {
                    });
                } catch (JsonProcessingException e) {
                    log.info(e.toString());
                    return;
                }
                BigDecimal amount = account.getAmount();
                BigDecimal percent;
                float bet = map.get(tariffs.get(tariffs.size() - 1));
                percent = amount.multiply(BigDecimal.valueOf(bet));
                percent = percent.divide(BigDecimal.valueOf(100), 5, RoundingMode.DOWN);
                percent = percent.divide(BigDecimal.valueOf(365), 5, RoundingMode.DOWN);
                amount = amount.add(percent);

                statement.setBigDecimal(1, amount.setScale(5, RoundingMode.DOWN));
                statement.setObject(2, newTime);
                statement.setLong(3, account.getId());
                statement.addBatch();
                count++;
                if (count % 1000 == 0 || count == accounts.size()) {
                    statement.executeBatch();
                }
            }
        } catch (SQLException e) {
            log.info(e.toString());
        }
    }

    private void accrual(List<SavingsAccount> list, Map<Long, Float> map, Function<List<SavingsAccount>
            , List<SavingsAccount>> checkTime, LocalDateTime newTime, List<Long> listId) {
        counter.incrementAndGet();

        if (checkTime.apply(list).isEmpty()) {
            return;
        }

        run(list, map, newTime);

        if (list.size() <= packetSizeForStream) {
            run(list, map, newTime);
        } else {
            for (List<SavingsAccount> subs : subList(list, packetSizeForStream)) {
                accrualExecutor.submit(() -> run(subs, map, newTime));
            }
        }

        list = savingsAccountRepository.findAllById(listId);
        List<SavingsAccount> repeat = checkTime.apply(list);
        if (repeat.isEmpty()) {
            return;
        }
        if (counter.get() >= 5) {
            return;
        }
        accrual(repeat, map, checkTime, newTime, repeat.stream().map(SavingsAccount::getId).toList());
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
