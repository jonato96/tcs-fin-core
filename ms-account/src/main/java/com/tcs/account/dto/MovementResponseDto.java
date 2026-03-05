package com.tcs.account.dto;

import com.tcs.account.domain.MovementType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementResponseDto(
        Long id,
        LocalDateTime date,
        MovementType movementType,
        BigDecimal amount,
        BigDecimal balanceBefore,
        BigDecimal balanceAfter,
        String accountNumber
) {}
