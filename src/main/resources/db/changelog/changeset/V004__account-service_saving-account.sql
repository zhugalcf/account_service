

CREATE TABLE savings_account (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    account_id bigint NOT NULL,
    last_interest_date timestamptz DEFAULT current_timestamp,
    version INT NOT NULL,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,

    CONSTRAINT fk_account_saving FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE TABLE tariff (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    "type" VARCHAR(20) NOT NULL,
    saving_account_id bigint NOT NULL,

    CONSTRAINT fk_account_tariff FOREIGN KEY (saving_account_id) REFERENCES savings_account (id)
);

CREATE TABLE rate (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    "percent" DECIMAL(5, 2) NOT NULL,
    tariff_id bigint NOT NULL,

    CONSTRAINT fk_tariff_rate FOREIGN KEY (tariff_id) REFERENCES tariff (id)
);
