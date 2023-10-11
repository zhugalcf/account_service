package faang.school.accountservice.service;

import faang.school.accountservice.dto.BalanceAuditDto;
import faang.school.accountservice.mapper.BalanceAuditMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.Balance;
import faang.school.accountservice.model.BalanceAudit;
import faang.school.accountservice.repository.BalanceAuditRepository;
import faang.school.accountservice.repository.BalanceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BalanceAuditServiceTest {

    @Mock
    private BalanceAuditRepository balanceAuditRepository;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private BalanceAuditMapper balanceAuditMapper;

    @InjectMocks
    private BalanceAuditService balanceAuditService;

    @Test
    public void createBalanceAuditTest() {
        Account account = new Account();
        account.setId(1L);
        Balance balance = new Balance();
        balance.setAccount(account);
        balance.setCurrentAuthorizationBalance(new BigDecimal("100.0"));
        balance.setCurrentActualBalance(new BigDecimal("200.0"));

        when(balanceRepository.findById(balance.getId())).thenReturn(Optional.of(balance));

        balanceAuditService = new BalanceAuditService(balanceAuditRepository, balanceRepository, balanceAuditMapper);
        balanceAuditService.createBalanceAudit(balance.getId());

        verify(balanceAuditRepository).save(any(BalanceAudit.class));
    }



    @Test
    public void getBalanceAuditsTest() {
        BalanceAudit balanceAudit = new BalanceAudit();
        balanceAudit.setId(1L);

        when(balanceAuditRepository.findById(1L)).thenReturn(Optional.of(balanceAudit));
        when(balanceAuditMapper.toBalance(balanceAudit)).thenReturn(new BalanceAuditDto());

        balanceAuditService = new BalanceAuditService(balanceAuditRepository, balanceRepository, balanceAuditMapper);
        BalanceAuditDto result = balanceAuditService.getBalanceAudits(1L);

        assertNotNull(result);
    }
}
