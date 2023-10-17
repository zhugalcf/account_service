CREATE TABLE balance_audit
(
    balance_id bigint,
    balance_audit_version INT NOT NULL,
    authorization_amount NUMERIC(10, 2) NOT NULL,
    actual_amount NUMERIC(10, 2) NOT NULL,
    operation_id bigint,
    created_at timestamptz NOT NULL,

    PRIMARY KEY (balance_id, balance_audit_version)
    CONSTRAINT fk_account_id FOREIGN KEY (balance_id) REFERENCES balance (id)
);

CREATE INDEX idx_balance_id ON balance_audit (balance_id);
CREATE INDEX idx_operation_id ON balance_audit (operation_id);