package com.tcs.account.service.impl;

import com.tcs.account.domain.Account;
import com.tcs.account.domain.AccountType;
import com.tcs.account.domain.exception.AccountNotFoundException;
import com.tcs.account.dto.AccountRequestDto;
import com.tcs.account.dto.AccountResponseDto;
import com.tcs.account.dto.CustomerResponseDto;
import com.tcs.account.mapper.AccountMapper;
import com.tcs.account.rabbit.CustomerRequestProducer;
import com.tcs.account.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountMapper accountMapper;
    @Mock
    private CustomerRequestProducer customerProducer;
    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void create_whenAccountNumberAlreadyExists_throwsException() {
        AccountRequestDto dto = new AccountRequestDto("478758", AccountType.SAVING, BigDecimal.valueOf(2000), true, 1L);

        when(accountRepository.existsByAccountNumber("478758")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> accountService.create(dto));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void create_whenCustomerIsInactive_throwsException() {
        AccountRequestDto dto = new AccountRequestDto("478758", AccountType.SAVING, BigDecimal.valueOf(2000), true, 1L);

        when(accountRepository.existsByAccountNumber("478758")).thenReturn(false);
        when(customerProducer.findCustomer(1L)).thenReturn(new CustomerResponseDto(1L, "Jose Lema", false));

        assertThrows(IllegalArgumentException.class, () -> accountService.create(dto));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void create_success_returnsAccountResponseDto() {
        AccountRequestDto dto = new AccountRequestDto("478758", AccountType.SAVING, BigDecimal.valueOf(2000), true, 1L);
        Account entity = Account.builder().id(1L).accountNumber("478758").accountType(AccountType.SAVING)
                .initialBalance(BigDecimal.valueOf(2000)).availableBalance(BigDecimal.valueOf(2000))
                .active(true).customerId(1L).build();
        AccountResponseDto expected = new AccountResponseDto(1L, "478758", AccountType.SAVING,
                BigDecimal.valueOf(2000), BigDecimal.valueOf(2000), true, 1L, "Jose Lema");

        when(accountRepository.existsByAccountNumber("478758")).thenReturn(false);
        when(customerProducer.findCustomer(1L)).thenReturn(new CustomerResponseDto(1L, "Jose Lema", true));
        when(accountMapper.toEntity(dto)).thenReturn(entity);
        when(accountRepository.save(entity)).thenReturn(entity);
        when(accountMapper.toResponseDto(entity, "Jose Lema")).thenReturn(expected);

        AccountResponseDto result = accountService.create(dto);

        assertNotNull(result);
        assertEquals("478758", result.accountNumber());
        assertEquals("Jose Lema", result.customerName());
    }

    @Test
    void getActiveAccount_whenAccountNotFound_throwsException() {
        when(accountRepository.findByAccountNumber("000000")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getActiveAccount("000000"));
    }

    @Test
    void getActiveAccount_whenAccountIsInactive_throwsException() {
        Account inactive = Account.builder().id(1L).accountNumber("495878").active(false).build();

        when(accountRepository.findByAccountNumber("495878")).thenReturn(Optional.of(inactive));

        assertThrows(IllegalArgumentException.class, () -> accountService.getActiveAccount("495878"));
    }
}
