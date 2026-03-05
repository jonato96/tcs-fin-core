package com.tcs.account.service;

import com.tcs.account.domain.Account;
import com.tcs.account.dto.AccountRequestDto;
import com.tcs.account.dto.AccountResponseDto;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    AccountResponseDto create(AccountRequestDto dto);
    List<AccountResponseDto> findAll();
    AccountResponseDto findByAccountNumber(String accountNumber);
    void delete(String accountNumber);
    // Uso interno por MovementService y ReportService
    Account getActiveAccount(String accountNumber);
    List<Account> findByCustomerId(Long customerId);
    void updateBalance(BigDecimal newBalance, Long id);
}
