package com.tcs.account.dto;

import com.tcs.account.domain.AccountType;
import java.math.BigDecimal;

public record AccountResponseDto(
        Long id,
        String accountNumber,
        AccountType accountType,
        BigDecimal initialBalance,
        BigDecimal availableBalance,
        Boolean active,
        Long customerId,
        String customerName
) {}
