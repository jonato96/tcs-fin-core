package com.tcs.account.dto;

import com.tcs.account.domain.AccountType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record AccountRequestDto(
        @NotBlank(message = "El número de cuenta es obligatorio")
        String accountNumber,

        @NotNull(message = "El tipo de cuenta es obligatorio")
        AccountType accountType,

        @NotNull(message = "El saldo inicial es obligatorio")
        @PositiveOrZero(message = "El saldo inicial no puede ser negativo")
        BigDecimal initialBalance,

        @NotNull(message = "El estado es obligatorio")
        Boolean active,

        @NotNull(message = "El cliente es obligatorio")
        Long customerId
) {}
