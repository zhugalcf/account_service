CREATE TABLE IF NOT EXISTS savings_account (
                                 id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
                                 tariff_history_id BIGINT,
                                 created_at timestamptz DEFAULT current_timestamp,
                                 updated_at timestamptz DEFAULT current_timestamp,
                                 closed_at timestamptz,
                                 last_update_calculation_at TIMESTAMP,
                                 version BIGINT NOT NULL,

                                 CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account (id),
                                 CONSTRAINT fk_tariff_history FOREIGN KEY (tariff_history_id) REFERENCES savingsAccountTariffHistory (id)
);

CREATE TABLE savings_account_tariff_history (
                                                id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                                savings_account_id BIGINT NOT NULL,
                                                tariff_id BIGINT NOT NULL,
                                                change_date timestamptz DEFAULT current_timestamp,

                                                CONSTRAINT fk_savings_account FOREIGN KEY (savings_account_id) REFERENCES savingsAccount (id),
                                                CONSTRAINT fk_tariff FOREIGN KEY (tariff_id) REFERENCES tariff (id)
);

CREATE TABLE IF NOT EXISTS tariff (
                        id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                        tariff_type VARCHAR(64) NOT NULL,
                        rate DECIMAL(2, 10) NOT NULL,

                        CONSTRAINT fk_tariff FOREIGN KEY (tariff_id) REFERENCES tariff (id)
);