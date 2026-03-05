package com.tcs.account.dto;

import com.tcs.account.domain.AccountType;
import com.tcs.account.domain.MovementType;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountStatementDto {
    private Long customerId;
    private String accountNumber;
    private AccountType accountType;
    private Double initialBalance;
    private Double availableBalance;
    private Boolean status;
    private List<MovementDetailDto> movements;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MovementDetailDto {
        private LocalDateTime date;
        private MovementType movementType;
        private Double amount;
        private Double balanceBefore;
        private Double balanceAfter;
    }
}
