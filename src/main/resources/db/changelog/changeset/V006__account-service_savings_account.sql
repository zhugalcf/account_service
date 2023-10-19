CREATE TABLE savings_account (
                                               id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
                                               account_id BIGINT NOT NULL,
                                               account_type INT NOT NULL,
                                               savings_account_status INT NOT NULL,
                                               created_at timestamptz DEFAULT current_timestamp,
                                               updated_at timestamptz DEFAULT current_timestamp,
                                               closed_at timestamptz,
                                               last_update_calculation_at timestamptz DEFAULT current_timestamp,
                                               version BIGINT NOT NULL,

                                               CONSTRAINT fk_savings_account_account FOREIGN KEY (account_id) REFERENCES Account(id)

    );

CREATE TABLE savings_account_tariff_history (
                                                id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                                savings_account_id BIGINT NOT NULL,
                                                tariff_id BIGINT NOT NULL,
                                                change_date timestamptz DEFAULT current_timestamp,

                                                PRIMARY KEY (savings_account_id, tariff_id),
                                                CONSTRAINT fk_tariff_history_savings_account FOREIGN KEY (savings_account_id) REFERENCES savings_account(id)
);

CREATE TABLE IF NOT EXISTS tariff (
                                      id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                      tariff_type VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS tariff_rates (
                                            tariff_id INT NOT NULL,
                                            rate DECIMAL(2, 10) NOT NULL,

                                            CONSTRAINT fk_tariff_rates_tariff FOREIGN KEY (tariff_id) REFERENCES tariff(id)
);