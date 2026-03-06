package com.tcs.account.domain.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String accountNumber) {
        super("Cuenta no encontrada: " + accountNumber);
    }
}