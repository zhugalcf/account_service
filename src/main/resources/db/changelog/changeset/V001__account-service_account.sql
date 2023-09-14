CREATE TABLE owner
(
    id         BIGSERIAL PRIMARY KEY,
    owner_type VARCHAR(255) NOT NULL,
    owner_id   BIGINT       NOT NULL
);

CREATE TABLE currency
(
    id            BIGSERIAL PRIMARY KEY,
    currency_name VARCHAR(3) UNIQUE NOT NULL
);

CREATE TABLE account
(
    id             BIGSERIAL PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL UNIQUE CHECK (length(account_number) >= 12 AND account_number SIMILAR TO '[0-9]+'),
    owner_id       BIGINT      NOT NULL,
    type           smallint    NOT NULL,
    currency_id    smallint    NOT NULL,
    status         smallint             DEFAULT 0,
    created_at     TIMESTAMPTZ          DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMPTZ          DEFAULT CURRENT_TIMESTAMP,
    closing_date   TIMESTAMPTZ,
    version        BIGINT      NOT NULL DEFAULT 0,

    CONSTRAINT fk_account_owner FOREIGN KEY (owner_id) REFERENCES owner (id),
    CONSTRAINT fk_account_currency FOREIGN KEY (currency_id) REFERENCES currency (id)
);

CREATE INDEX index_number_payment ON account (account_number);
CREATE INDEX index_owner_id ON account (owner_id);
