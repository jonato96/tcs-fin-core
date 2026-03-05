package com.tcs.account.util;

import com.tcs.account.domain.Account;
import com.tcs.account.domain.AccountType;
import com.tcs.account.domain.Movement;
import com.tcs.account.domain.MovementType;
import com.tcs.account.dto.CustomerResponseDto;
import com.tcs.account.dto.ReportLineDto;
import com.tcs.account.rabbit.CustomerRequestProducer;
import com.tcs.account.service.AccountService;
import com.tcs.account.service.MovementService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private AccountService accountService;
    @Mock
    private MovementService movementService;
    @Mock
    private CustomerRequestProducer customerProducer;
    @InjectMocks
    private ReportService reportService;

    @Test
    void generateReport_whenEndDateBeforeStartDate_throwsException() {
        assertThrows(IllegalArgumentException.class, () ->
                reportService.generateReport(1L, LocalDate.of(2022, 12, 31), LocalDate.of(2022, 1, 1)));
    }

    @Test
    void generateReport_whenCustomerIsInactive_throwsException() {
        when(customerProducer.findCustomer(1L)).thenReturn(new CustomerResponseDto(1L, "Jose Lema", false));

        assertThrows(IllegalArgumentException.class, () ->
                reportService.generateReport(1L, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 12, 31)));
    }

    @Test
    void generateReport_whenCustomerHasNoAccounts_throwsException() {
        when(customerProducer.findCustomer(1L)).thenReturn(new CustomerResponseDto(1L, "Jose Lema", true));
        when(accountService.findByCustomerId(1L)).thenReturn(List.of());

        assertThrows(IllegalArgumentException.class, () ->
                reportService.generateReport(1L, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 12, 31)));
    }

    @Test
    void generateReport_success_returnsReportLines() {
        LocalDate start = LocalDate.of(2022, 2, 1);
        LocalDate end = LocalDate.of(2022, 2, 28);

        Account account = Account.builder()
                .id(1L).accountNumber("225487").accountType(AccountType.CHECKING)
                .initialBalance(BigDecimal.valueOf(100)).availableBalance(BigDecimal.valueOf(700))
                .active(true).customerId(1L).build();

        Movement movement = Movement.builder()
                .id(1L).date(LocalDateTime.of(2022, 2, 10, 10, 0))
                .movementType(MovementType.CREDIT)
                .amount(BigDecimal.valueOf(600))
                .balanceBefore(BigDecimal.valueOf(100))
                .balanceAfter(BigDecimal.valueOf(700))
                .account(account).build();

        when(customerProducer.findCustomer(1L)).thenReturn(new CustomerResponseDto(1L, "Marianela Montalvo", true));
        when(accountService.findByCustomerId(1L)).thenReturn(List.of(account));
        when(movementService.findByAccountAndDates(eq(1L), any(), any())).thenReturn(List.of(movement));

        List<ReportLineDto> result = reportService.generateReport(1L, start, end);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Marianela Montalvo", result.get(0).cliente());
        assertEquals("225487", result.get(0).numeroCuenta());
        assertEquals(BigDecimal.valueOf(700), result.get(0).saldoDisponible());
    }
}
