CREATE TABLE BalanceAudit
(
    balance_audit_id bigint PRIMARY KEY,
    balance_id bigint NOT NULL,
    balance_audit_version INT NOT NULL,
    authorization_amount NUMERIC(10, 2),
    actual_amount NUMERIC(10, 2),
    operation_id bigint,
    audit_timestamp timestamptz DEFAULT current_timestamp,

    CONSTRAINT fk_account_id FOREIGN KEY (balance_id) REFERENCES balance (id)
);