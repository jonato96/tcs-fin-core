package com.tcs.account.domain.exception;

public class MovementNotFoundException extends RuntimeException {
    public MovementNotFoundException(Long id) {
        super("Movimiento no encontrado: " + id);
    }
}