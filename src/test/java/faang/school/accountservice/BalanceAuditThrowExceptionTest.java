package faang.school.accountservice;

import faang.school.accountservice.mapper.BalanceAuditMapper;
import faang.school.accountservice.model.Account;
import faang.school.accountservice.model.BalanceAudit;
import faang.school.accountservice.repository.BalanceAuditRepository;
import faang.school.accountservice.repository.BalanceRepository;
import faang.school.accountservice.service.BalanceAuditService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BalanceAuditThrowExceptionTest {

    @Mock
    private BalanceAuditRepository balanceAuditRepository;

    @Mock
    private BalanceRepository balanceRepository;

    @Mock
    private BalanceAuditMapper balanceAuditMapper;

    @InjectMocks
    private BalanceAuditService balanceAuditService;

    @Test
    public void createBalanceAuditThrowExceptionTest() {
        Account account = new Account();
        account.setId(1L);

        when(balanceRepository.findBalanceByAccountId(account.getId())).thenReturn(Optional.empty());

        balanceAuditService = new BalanceAuditService(balanceAuditRepository, balanceRepository, balanceAuditMapper);

        assertThrows(EntityNotFoundException.class, () -> balanceAuditService.createBalanceAudit(account));

        verify(balanceAuditRepository, never()).save(Mockito.any(BalanceAudit.class));
    }

    @Test
    public void getBalanceAuditsThrowExceptionTest() {

        when(balanceAuditRepository.findById(1L)).thenReturn(Optional.empty());

        balanceAuditService = new BalanceAuditService(balanceAuditRepository, balanceRepository, balanceAuditMapper);

        assertThrows(EntityNotFoundException.class,
                () -> balanceAuditService.getBalanceAudits(1L));
    }
}
