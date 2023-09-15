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
        Balance balance = balanceExistsValidation(balanceId);

        return balanceMapper.toDto(balance);
    }

    @Transactional
    public BalanceDto updateBalance(UpdateBalanceDto updateBalanceDto, Long balanceId) {
        validateOwnership(updateBalanceDto, balanceId);
        Balance balance = balanceExistsValidation(updateBalanceDto.getId());

        BigDecimal currentBalance = balance.getCurrentBalance();
        BigDecimal deposit = updateBalanceDto.getDeposit();

        BigDecimal updatedBalance = currentBalance.add(deposit);

        balance.setCurrentBalance(updatedBalance);

        Balance savedBalance = balanceRepository.save(balance);

        return balanceMapper.toDto(savedBalance);
    }

    private void validateOwnership(UpdateBalanceDto updateBalanceDto, Long balanceId) {
        if (!Objects.equals(balanceId, updateBalanceDto.getId())) {
            throw new IllegalArgumentException("You can only update your own balance");
        }
    }

    private Balance balanceExistsValidation(Long balanceId) {
        return balanceRepository.findById(balanceId)
                .orElseThrow(() -> new EntityNotFoundException("Balance with id: " + balanceId + " not found"));
    }

    public BalanceDto createBalance(BalanceDto balanceDto) {
        Balance entity = balanceMapper.toEntity(balanceDto);
        balanceRepository.save(entity);
        return balanceMapper.toDto(entity);
    }
}