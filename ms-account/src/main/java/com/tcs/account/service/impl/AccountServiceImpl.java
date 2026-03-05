package com.tcs.account.service.impl;

import com.tcs.account.domain.Account;
import com.tcs.account.domain.exception.AccountNotFoundException;
import com.tcs.account.dto.AccountRequestDto;
import com.tcs.account.dto.AccountResponseDto;
import com.tcs.account.dto.CustomerResponseDto;
import com.tcs.account.mapper.AccountMapper;
import com.tcs.account.rabbit.CustomerRequestProducer;
import com.tcs.account.repository.AccountRepository;
import com.tcs.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerRequestProducer customerProducer;

    @Override
    @Transactional
    public AccountResponseDto create(AccountRequestDto dto) {
        if (accountRepository.existsByAccountNumber(dto.accountNumber())) {
            throw new IllegalArgumentException("El número de cuenta ya existe: " + dto.accountNumber());
        }
        CustomerResponseDto customer = customerProducer.findCustomer(dto.customerId());
        if (customer == null || !Boolean.TRUE.equals(customer.active())) {
            throw new IllegalArgumentException("Cliente no encontrado o inactivo: " + dto.customerId());
        }
        Account account = accountMapper.toEntity(dto);
        return accountMapper.toResponseDto(accountRepository.save(account), customer.name());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponseDto> findAll() {
        return accountRepository.findAll().stream()
                .map(a -> accountMapper.toResponseDto(a, null))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponseDto findByAccountNumber(String accountNumber) {
        Account account = getAccountOrThrow(accountNumber);
        CustomerResponseDto customer = customerProducer.findCustomer(account.getCustomerId());
        return accountMapper.toResponseDto(account, customer != null ? customer.name() : null);
    }

    @Override
    @Transactional
    public void delete(String accountNumber) {
        Account account = getAccountOrThrow(accountNumber);
        accountRepository.inactivateAccount(account.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Account getActiveAccount(String accountNumber) {
        Account account = getAccountOrThrow(accountNumber);
        if (!Boolean.TRUE.equals(account.getActive())) {
            throw new IllegalArgumentException("La cuenta está inactiva: " + accountNumber);
        }
        return account;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Account> findByCustomerId(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional
    public void updateBalance(BigDecimal newBalance, Long id) {
        accountRepository.updateBalance(newBalance, id);
    }

    private Account getAccountOrThrow(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }
}
