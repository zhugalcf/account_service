CREATE TABLE savings_account (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    savings_account_number VARCHAR(20),
    account_id BIGINT NOT NULL,
    balance DECIMAL(19, 2) NOT NULL,
    last_interest_date timestamptz,
    version INT NOT NULL,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz,

    CONSTRAINT fk_account_savings FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE TABLE tariff (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    tariff_type VARCHAR(20) NOT NULL,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz,

    CONSTRAINT idx_unique_tariff_type UNIQUE (tariff_type)
);

CREATE TABLE tariff_history (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    savings_account_id BIGINT NOT NULL,
    tariff_id BIGINT NOT NULL,
    last_modified_date timestamptz,

     CONSTRAINT savings_account_fk FOREIGN KEY (savings_account_id) REFERENCES savings_account (id),
     CONSTRAINT tariff_fk FOREIGN KEY (tariff_id) REFERENCES tariff (id)
);

CREATE TABLE rate (
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    rate_percent float NOT NULL,
    tariff_id BIGINT NOT NULL,

    CONSTRAINT fk_tariff_rate FOREIGN KEY (tariff_id) REFERENCES tariff (id)
);

CREATE UNIQUE INDEX idx_unique_number ON savings_account (savings_account_number);