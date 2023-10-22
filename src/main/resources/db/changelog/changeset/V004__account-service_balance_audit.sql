DROP TABLE IF EXISTS balance_audit;
CREATE TABLE balance_audit (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    account_id BIGINT REFERENCES account(id),
    balance_version BIGINT,
    authorization_balance NUMERIC(10, 2),
    current_balance NUMERIC(10, 2),
    operation_id BIGINT,
    created_at TIMESTAMPTZ,

    CONSTRAINT fk_account_id FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
);