package com.tcs.account.dto;

import com.tcs.account.domain.MovementType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record MovementRequestDto(
        @NotBlank(message = "El número de cuenta es obligatorio")
        String accountNumber,

        @NotNull(message = "El valor del movimiento es obligatorio")
        @Positive(message = "El valor debe ser mayor a 0")
        BigDecimal value,

        @NotNull(message = "El tipo de movimiento es obligatorio")
        MovementType movementType
) {}
