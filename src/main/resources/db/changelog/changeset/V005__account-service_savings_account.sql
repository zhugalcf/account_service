CREATE TABLE IF NOT EXISTS savings_account (
                                 id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
                                 tariff_history_id BIGINT,
                                 created_at TIMESTAMP NOT NULL,
                                 updated_at TIMESTAMP,
                                 closed_at TIMESTAMP,
                                 last_update_calculation_at TIMESTAMP,
                                 version BIGINT NOT NULL,

                                 CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account (id),
                                 CONSTRAINT fk_tariff_history FOREIGN KEY (tariff_history_id) REFERENCES savingsAccountTariffHistory (id)
);

CREATE TABLE savings_account_tariff_history (
                                                id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                                savings_account_id BIGINT NOT NULL,
                                                tariff_id BIGINT NOT NULL,
                                                change_date TIMESTAMP NOT NULL,

                                                CONSTRAINT fk_savings_account FOREIGN KEY (savings_account_id) REFERENCES savingsAccount (id),
                                                CONSTRAINT fk_tariff FOREIGN KEY (tariff_id) REFERENCES tariff (id)
);

CREATE TABLE IF NOT EXISTS tariff (
                        id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                        tariff_type SMALLINT DEFAULT 0 NOT NULL,
                        rate DOUBLE PRECISION,

                        CONSTRAINT fk_tariff FOREIGN KEY (tariff_id) REFERENCES tariff (id)
);