package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.dto.UpdateBalanceDto;
import faang.school.accountservice.mapper.BalanceMapper;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.repository.BalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BalanceService {
    private final BalanceRepository balanceRepository;
    private final BalanceMapper balanceMapper;

    @Transactional(readOnly = true)
    public BalanceDto getBalance(Long balanceId) {
        Balance balance = loadBalanceOrThrow(balanceId);

        log.info("Balance with id: {} fetched", balanceId);
        return balanceMapper.toDto(balance);
    }

    @Transactional
    public BalanceDto updateBalance(UpdateBalanceDto updateBalanceDto, Long balanceId) {
        validateOwnership(updateBalanceDto, balanceId);
        Balance balance = loadBalanceOrThrow(updateBalanceDto.getId());

        BigDecimal currentBalance = balance.getCurrentBalance();
        BigDecimal deposit = updateBalanceDto.getDeposit();

        BigDecimal updatedBalance = currentBalance.add(deposit);

        balance.setCurrentBalance(updatedBalance);

        Balance savedBalance = balanceRepository.save(balance);

        log.info("Balance with id: {} updated", balanceId);
        return balanceMapper.toDto(savedBalance);
    }

    private void validateOwnership(UpdateBalanceDto updateBalanceDto, Long balanceId) {
        if (!Objects.equals(balanceId, updateBalanceDto.getId())) {
            throw new IllegalArgumentException("You can only update your own balance");
        }
    }

    private Balance loadBalanceOrThrow(Long balanceId) {
        return balanceRepository.findById(balanceId)
                .orElseThrow(() -> new EntityNotFoundException("Balance with id: " + balanceId + " not found"));
    }
}