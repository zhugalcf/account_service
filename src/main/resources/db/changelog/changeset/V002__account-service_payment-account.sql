CREATE TABLE account
(
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    number VARCHAR(20),
    owner_type VARCHAR(20) NOT NULL,
    owner_id BIGINT NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    currency CHARACTER(3)  NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,
    closed_at TIMESTAMP,
    version INT NOT NULL
);

CREATE INDEX owner_type_and_ididx ON account(owner_type, owner_id);
CREATE INDEX idx_number ON account(number);