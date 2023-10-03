package faang.school.accountservice.mapper;

import faang.school.accountservice.dto.BalanceDto;
import faang.school.accountservice.entity.balance.Balance;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BalanceMapperTest {

    private final BalanceMapper balanceMapper = Mappers.getMapper(BalanceMapper.class);

    @Test
    public void testToEntity() {
        BalanceDto balanceDto = BalanceDto.builder()
                .authorizationBalance(BigDecimal.valueOf(100.0))
                .actualBalance(BigDecimal.valueOf(100.0))
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();

        Balance balance = balanceMapper.toEntity(balanceDto);

        assertNotNull(balance);
        assertAll(
                () -> assertEquals(balanceDto.getAuthorizationBalance(), balance.getAuthorizationBalance()),
                () -> assertEquals(balanceDto.getActualBalance(), balance.getActualBalance()),
                () -> assertEquals(balanceDto.getCreatedAt(), balance.getCreatedAt()),
                () -> assertEquals(balanceDto.getUpdatedAt(), balance.getUpdatedAt())
        );
    }

    @Test
    public void testToDto() {
        Balance balance = new Balance();
        balance.setAuthorizationBalance(BigDecimal.valueOf(100.0));
        balance.setActualBalance(BigDecimal.valueOf(100.0));
        balance.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        balance.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        BalanceDto balanceDto = balanceMapper.toDto(balance);

        assertNotNull(balanceDto);
        assertAll(
                () -> assertEquals(balance.getAuthorizationBalance(), balanceDto.getAuthorizationBalance()),
                () -> assertEquals(balance.getActualBalance(), balanceDto.getActualBalance()),
                () -> assertEquals(balance.getCreatedAt(), balanceDto.getCreatedAt()),
                () -> assertEquals(balance.getUpdatedAt(), balanceDto.getUpdatedAt())
        );
    }

    @Test
    public void testUpdateBalanceFromBalanceDto() {
        Balance balance = new Balance();
        balance.setAuthorizationBalance(BigDecimal.valueOf(100.0));
        balance.setActualBalance(BigDecimal.valueOf(100.0));
        balance.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        balance.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        BalanceDto balanceDto = BalanceDto.builder()
                .authorizationBalance(BigDecimal.valueOf(200.0))
                .actualBalance(BigDecimal.valueOf(200.0))
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .updatedAt(new Timestamp(System.currentTimeMillis()))
                .build();

        balanceMapper.updateBalanceFromBalanceDto(balanceDto, balance);

        assertAll(
                () -> assertEquals(balance.getAuthorizationBalance(), balanceDto.getAuthorizationBalance()),
                () -> assertEquals(balance.getActualBalance(), balanceDto.getActualBalance()),
                () -> assertEquals(balance.getCreatedAt(), balanceDto.getCreatedAt()),
                () -> assertEquals(balance.getUpdatedAt(), balanceDto.getUpdatedAt())
        );
    }
}
