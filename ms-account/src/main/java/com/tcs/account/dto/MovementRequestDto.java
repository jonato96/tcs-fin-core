package com.tcs.account.dto;

import com.tcs.account.domain.MovementType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record MovementRequestDto(
        @NotBlank(message = "El número de cuenta es obligatorio")
        @Pattern(regexp = "^[0-9]{10}$", message = "El número de cuenta debe tener exactamente 10 dígitos")
        String accountNumber,

        @NotNull(message = "El valor del movimiento es obligatorio")
        @Positive(message = "El valor debe ser mayor a 0")
        @Digits(integer = 8, fraction = 2, message = "El valor debe tener máximo 2 decimales")
        BigDecimal value,

        @NotNull(message = "El tipo de movimiento es obligatorio")
        MovementType movementType
) {}
