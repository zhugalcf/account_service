CREATE TABLE tariff
(
    id           BIGSERIAL PRIMARY KEY,
    type         SMALLINT     NOT NULL,
    rate         DECIMAL(4, 2) NOT NULL,
    rate_history VARCHAR(255) NOT NULL
);

CREATE TABLE savings_account
(
    id                 BIGSERIAL PRIMARY KEY,
    account_id         BIGINT       NOT NULL,
    history_tariff     VARCHAR(255) NOT NULL,
    latest_report_date TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP,
    version            BIGINT       NOT NULL DEFAULT 0,
    created_at         TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMPTZ           DEFAULT CURRENT_TIMESTAMP,
    tariff_id          BIGINT       NOT NULL,

    CONSTRAINT fk_savings_account_on_account FOREIGN KEY (account_id) REFERENCES account (id),
    CONSTRAINT fk_savings_account_on_tariff FOREIGN KEY (tariff_id) REFERENCES tariff (id)
);