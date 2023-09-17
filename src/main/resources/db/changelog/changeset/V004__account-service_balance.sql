CREATE TABLE balance
(
    id                    SERIAL PRIMARY KEY,
    account_id            BIGINT         NOT NULL,
    authorization_balance DECIMAL(10, 2) NOT NULL,
    actual_balance        DECIMAL(10, 2) NOT NULL,
    created_at            TIMESTAMP      NOT NULL,
    updated_at            TIMESTAMP      NOT NULL,
    version               BIGINT         NOT NULL,
    FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE INDEX idx_balance_account_id ON balance (account_id);
