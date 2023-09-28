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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccrualToScore {
    private final ObjectMapper objectMapper;
    private final ExecutorService executor;
    private final SavingsAccountRepository savingsAccountRepository;
    private final TariffRepository tariffRepository;
    private final AtomicInteger counter = new AtomicInteger(0);

    @Scheduled(cron = "0 0 6 * * *")
    public void accrualPercent() {
        counter.set(0);
        LocalDateTime newDateTime = LocalDateTime.now().minus(24, TimeUnit.HOURS.toChronoUnit());
        List<SavingsAccount> savingsAccounts = savingsAccountRepository.findAll();
        Function<List<SavingsAccount>, List<SavingsAccount>> checkTime = (lists) -> lists.stream()
                .filter(time -> time.getLastTimeOfAccrual().isBefore(newDateTime)).toList();
        if (checkTime.apply(savingsAccounts).isEmpty()) {
            return;
        }
        List<Tariff> tariffs = tariffRepository.findAll();
        Map<Long, Float> map = new HashMap<>(tariffs.size());
        for (Tariff tariff : tariffs) {
            map.put(tariff.getId(), tariff.getBet());
        }
        accrual(savingsAccounts, map, checkTime);
    }

    public SavingsAccount run(SavingsAccount savingsAccount, Map<Long, Float> map) {
        List<Long> tariffs;
        try {
            tariffs = objectMapper.readValue(savingsAccount.getTariffHistory(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            log.info(e.toString());
            return null;
        }
        BigDecimal amount = savingsAccount.getAmount();
        BigDecimal percent;
        float bet = map.get(tariffs.get(tariffs.size() - 1));
        percent = amount.multiply(BigDecimal.valueOf(bet));
        percent = percent.divide(BigDecimal.valueOf(100), 5, RoundingMode.DOWN);
        percent = percent.divide(BigDecimal.valueOf(365), 5, RoundingMode.DOWN);
        amount = amount.add(percent);
        savingsAccount.setAmount(amount.setScale(5, RoundingMode.DOWN));
        savingsAccount.setLastTimeOfAccrual(LocalDateTime.now());
        return savingsAccountRepository.save(savingsAccount);
    }

    private void accrual(List<SavingsAccount> list, Map<Long, Float> map, Function<List<SavingsAccount>, List<SavingsAccount>> checkTime) {
        counter.incrementAndGet();
        List<Future<SavingsAccount>> futures = new ArrayList<>(list.size());
        list.forEach(savingsAccount -> futures.add(executor.submit(() -> run(savingsAccount, map))));
        for (Future<SavingsAccount> future : futures) {
            try {
                future.get();
            } catch (ExecutionException | InterruptedException ignored) {
            }
        }
        list = savingsAccountRepository.findAll();
        List<SavingsAccount> repeat = checkTime.apply(list);
        if (repeat.isEmpty()) {
            return;
        }
        if (counter.get() >= 5) {
            log.info("accrual error" + repeat);
            return;
        }
        accrual(repeat, map, checkTime);
    }
}
