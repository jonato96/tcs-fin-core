CREATE TABLE persons (
    id              BIGSERIAL       PRIMARY KEY,
    name            VARCHAR(100)    NOT NULL,
    gender          VARCHAR(10)     NOT NULL,
    age             INTEGER         NOT NULL,
    identification  VARCHAR(20)     NOT NULL UNIQUE,
    address         VARCHAR(200)    NOT NULL,
    phone           VARCHAR(15)     NOT NULL
);

CREATE TABLE customers (
    id              BIGINT          PRIMARY KEY,
    password        VARCHAR(255)    NOT NULL,
    active          BOOLEAN         NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_customers_person FOREIGN KEY (id) REFERENCES persons(id)
);

CREATE INDEX idx_persons_identification ON persons(identification);
