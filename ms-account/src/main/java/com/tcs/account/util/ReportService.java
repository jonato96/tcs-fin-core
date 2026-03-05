package com.tcs.account.util;

import com.tcs.account.domain.Account;
import com.tcs.account.domain.Movement;
import com.tcs.account.dto.CustomerResponseDto;
import com.tcs.account.dto.ReportLineDto;
import com.tcs.account.rabbit.CustomerRequestProducer;
import com.tcs.account.service.AccountService;
import com.tcs.account.service.MovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final AccountService accountService;
    private final MovementService movementService;
    private final CustomerRequestProducer customerProducer;

    @Transactional(readOnly = true)
    public List<ReportLineDto> generateReport(Long customerId, LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("El rango de fechas es incorrecto.");
        }

        CustomerResponseDto customer = customerProducer.findCustomer(customerId);
        if (customer == null || !Boolean.TRUE.equals(customer.active())) {
            throw new IllegalArgumentException("Cliente no encontrado o inactivo: " + customerId);
        }

        List<Account> accounts = accountService.findByCustomerId(customerId);
        if (accounts.isEmpty()) {
            throw new IllegalArgumentException("El cliente no tiene cuentas asociadas.");
        }

        return accounts.stream()
                .flatMap(account -> {
                    List<Movement> movements = movementService.findByAccountAndDates(
                            account.getId(),
                            startDate.atStartOfDay(),
                            endDate.atTime(LocalTime.MAX));
                    return movements.stream().map(m -> new ReportLineDto(
                            m.getDate(),
                            customer.name(),
                            account.getAccountNumber(),
                            account.getAccountType().getDisplayName(),
                            account.getInitialBalance(),
                            account.getActive(),
                            m.getAmount(),
                            m.getBalanceAfter()
                    ));
                })
                .toList();
    }
}
