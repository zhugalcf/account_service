CREATE TABLE savings_account (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    account_id bigint NOT NULL,
    last_interest_date timestamptz DEFAULT current_timestamp,
    version INT NOT NULL,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,

    CONSTRAINT fk_account_savings FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE TABLE tariff (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    "type" VARCHAR(20) NOT NULL
);

CREATE TABLE tariff_history (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    savings_account_id bigint NOT NULL,
    tariff_id bigint NOT NULL,
    last_modified_date timestamptz DEFAULT current_timestamp,

     CONSTRAINT savings_account_fk FOREIGN KEY (savings_account_id) REFERENCES savings_account (id),
     CONSTRAINT tariff_fk FOREIGN KEY (tariff_id) REFERENCES tariff (id)
);

CREATE TABLE rate (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    "percent" DECIMAL(5, 2) NOT NULL,
    tariff_id bigint NOT NULL,

    CONSTRAINT fk_tariff_rate FOREIGN KEY (tariff_id) REFERENCES tariff (id)
);
