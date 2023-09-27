package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.dto.UpdateBalanceDto;
import faang.school.accountservice.mapper.AccountMapper;
import faang.school.accountservice.mapper.BalanceMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.repository.AccountRepository;
import faang.school.accountservice.repository.BalanceRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private BalanceRepository balanceRepository;
    @Spy
    private AccountMapper accountMapper = Mappers.getMapper(AccountMapper.class);
    @Spy
    private BalanceMapper balanceMapper = Mappers.getMapper(BalanceMapper.class);
    @InjectMocks
    private BalanceService balanceService;

    @Test
    void testGetBalance() {
        Long balanceId = 1L;
        Balance balance = createSampleBalance(balanceId);
        when(balanceRepository.findById(balanceId)).thenReturn(Optional.of(balance));

        BalanceDto result = balanceService.getBalance(balanceId);

        assertNotNull(result);
        assertEquals(balanceId, result.getId());
        verify(balanceRepository, times(1)).findById(balanceId);
    }

    @Test
    void testGetBalanceNotFound() {
        Long balanceId = 1L;
        when(balanceRepository.findById(balanceId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> balanceService.getBalance(balanceId));

        assertEquals("Balance with id: " + balanceId + " not found", exception.getMessage());
        verify(balanceRepository, times(1)).findById(balanceId);
    }

    @Test
    void testUpdateBalance() {
        Long balanceId = 1L;
        UpdateBalanceDto updateBalanceDto = new UpdateBalanceDto();
        updateBalanceDto.setId(balanceId);
        updateBalanceDto.setDeposit(BigDecimal.TEN);

        Balance balance = createSampleBalance(balanceId);
        when(balanceRepository.findById(balanceId)).thenReturn(Optional.of(balance));
        when(balanceRepository.save(any(Balance.class))).thenReturn(balance);

        BalanceDto result = balanceService.updateBalance(updateBalanceDto, balanceId);

        assertNotNull(result);
        assertEquals(balanceId, result.getId());
        assertEquals(BigDecimal.TEN, result.getCurrentBalance());
        verify(balanceRepository, times(1)).findById(balanceId);
        verify(balanceRepository, times(1)).save(balance);
    }

    @Test
    void testUpdateBalanceNotFound() {
        Long balanceId = 1L;
        UpdateBalanceDto updateBalanceDto = new UpdateBalanceDto();
        updateBalanceDto.setId(balanceId);
        updateBalanceDto.setDeposit(BigDecimal.TEN);

        when(balanceRepository.findById(balanceId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> balanceService.updateBalance(updateBalanceDto, balanceId));
        assertEquals("Balance with id: " + balanceId + " not found", exception.getMessage());

        verify(balanceRepository, times(1)).findById(balanceId);
        verify(balanceRepository, never()).save(any(Balance.class));
    }

    @Test
    void testUpdateBalanceOwnershipValidationFailure() {
        Long balanceId = 1L;
        UpdateBalanceDto updateBalanceDto = new UpdateBalanceDto();
        updateBalanceDto.setId(2L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> balanceService.updateBalance(updateBalanceDto, balanceId));
        assertEquals("You can only update your own balance", exception.getMessage());

        verify(balanceRepository, never()).save(any(Balance.class));
    }

    private Balance createSampleBalance(Long balanceId) {
        return Balance.builder()
                .id(balanceId)
                .account(Account.builder().id(1L).build())
                .authorizationBalance(BigDecimal.ZERO)
                .currentBalance(BigDecimal.ZERO)
                .version(0L)
                .build();
    }
}