CREATE TABLE accounts (
    id                  BIGSERIAL       PRIMARY KEY,
    account_number      VARCHAR(10)     NOT NULL UNIQUE,
    account_type        VARCHAR(20)     NOT NULL,
    initial_balance     DECIMAL(10,2)   NOT NULL,
    available_balance   DECIMAL(10,2)   NOT NULL,
    active              BOOLEAN         NOT NULL DEFAULT TRUE,
    customer_id         BIGINT          NOT NULL
);

CREATE TABLE movements (
    id              BIGSERIAL      PRIMARY KEY,
    created_date    TIMESTAMP      NOT NULL,
    movement_type   VARCHAR(10)    NOT NULL,
    amount          DECIMAL(10,2)  NOT NULL,
    balance_before  DECIMAL(10,2)  NOT NULL,
    balance_after   DECIMAL(10,2)  NOT NULL,
    account_id      BIGINT         NOT NULL,
    CONSTRAINT fk_movements_account FOREIGN KEY (account_id) REFERENCES accounts(id)
);

CREATE INDEX idx_accounts_customer_id   ON accounts(customer_id);
CREATE INDEX idx_accounts_number        ON accounts(account_number);
CREATE INDEX idx_movements_account_id   ON movements(account_id);
CREATE INDEX idx_movements_created_date ON movements(created_date);