package com.tcs.account.domain;

public enum AccountType {
    SAVING("Ahorros"),
    CHECKING("Corriente");

    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
