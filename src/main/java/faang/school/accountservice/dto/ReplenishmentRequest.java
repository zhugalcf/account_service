package faang.school.accountservice.dto;

import faang.school.accountservice.enums.Currency;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ReplenishmentRequest(
        @NotNull
        String number,

        @NotNull
        @Min(1)
        BigDecimal sum,

        @NotNull
        Currency currency
) {
}



