CREATE TABLE IF NOT EXISTS owner
(
    id BIGSERIAL PRIMARY KEY,
    owner_type VARCHAR(255) NOT NULL,
    owner_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS account
(
    id             BIGSERIAL PRIMARY KEY,
    number_payment VARCHAR(20) NOT NULL UNIQUE CHECK (length(number_payment) >= 12 AND number_payment SIMILAR TO '[0-9]+'),
    owner_id       BIGINT NOT NULL,
    type           VARCHAR(255) NOT NULL,
    currency       VARCHAR(255) NOT NULL,
    status         VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    closing_date   TIMESTAMP,
    version        BIGINT,

    CONSTRAINT fk_account_owner FOREIGN KEY (owner_id) REFERENCES owner (id)
);

CREATE INDEX index_number_payment_owner_id ON account (number_payment, owner_id);
