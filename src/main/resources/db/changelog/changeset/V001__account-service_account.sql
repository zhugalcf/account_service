CREATE TABLE owner (
    id         BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    type       SMALLINT    DEFAULT 0 NOT NULL,
    owner_id   BIGINT                NOT NULL UNIQUE,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp
);

CREATE TABLE account (
    id             BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    owner_id       BIGINT NOT NULL,
    account_type   SMALLINT DEFAULT 0 NOT NULL,
    currency       SMALLINT DEFAULT 0 NOT NULL,
    account_status SMALLINT DEFAULT 0 NOT NULL,
    created_at     timestamptz DEFAULT current_timestamp,
    updated_at     timestamptz DEFAULT current_timestamp,
    closed_at      timestamptz,
    version        BIGINT DEFAULT 0,

    CONSTRAINT fk_account_owner FOREIGN KEY (owner_id) REFERENCES owner (id)
);

CREATE INDEX idx_owner_id ON account (owner_id);
