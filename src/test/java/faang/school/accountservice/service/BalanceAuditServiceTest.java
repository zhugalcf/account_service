package faang.school.accountservice.service;

import com.fasterxml.jackson.datatype.jsr310.ser.ZoneIdSerializer;
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
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BalanceAuditServiceTest {
    @Mock
    private BalanceRepository balanceRepository;
    @Mock
    private BalanceAuditRepository balanceAuditRepository;
    @Mock
    private BalanceAuditMapper balanceAuditMapper;
    @InjectMocks
    private BalanceAuditService balanceAuditService;

    @Test
    public void testSnapshotBalanceAudit() {
        Account account = Account.builder().id(1L).build();
        Balance balance = Balance.builder().id(2L).authorizationBalance(new BigDecimal(1))
                .currentBalance(new BigDecimal(2)).version(12L).build();
        BalanceAudit balanceAudit = BalanceAudit.builder().balanceVersion(12L)
                .authorizationBalance(new BigDecimal(1)).currentBalance(new BigDecimal(2))
                .operationId(null).createdAt(ZonedDateTime.now().truncatedTo(ChronoUnit.SECONDS)).account(account).build();
        when(balanceRepository.findByAccount(account)).thenReturn(Optional.ofNullable(balance));

        balanceAuditService.snapshotBalanceAudit(account);

        verify(balanceRepository, times(1)).findByAccount(account);
        verify(balanceAuditRepository, times(1)).save(balanceAudit);
    }

    @Test
    public void getBalanceAuditsTest() {
        Long accountId = 1L;
        List<BalanceAudit> balanceAudits = new ArrayList<>();
        List<BalanceAuditDto> balanceAuditDtos = new ArrayList<>();
        when(balanceAuditRepository.findAllByAccountId(accountId)).thenReturn(balanceAudits);
        when(balanceAuditMapper.toDtoList(balanceAudits)).thenReturn(balanceAuditDtos);
        List<BalanceAuditDto> result = balanceAuditService.getBalanceAudits(accountId);
        assertEquals(balanceAuditDtos, result);
    }
}