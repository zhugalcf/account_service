CREATE TABLE request (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    idempotency_key UUID NOT NULL,
    owner_id bigint NOT NULL,
    request_type VARCHAR(255) NOT NULL,
    lock_value VARCHAR(255),
    is_open BOOLEAN NOT NULL,
    input_data TEXT,
    status VARCHAR(255) NOT NULL,
    status_details VARCHAR,
    created_at timestamptz NOT NULL,
    updated_at timestamptz NOT NULL,
    version bigint NOT NULL
);

CREATE INDEX idempotency_key_index ON request (idempotency_key);
CREATE INDEX owner_index ON request (owner_id);
CREATE UNIQUE INDEX open_request_index ON request (lock_value) WHERE is_open AND lock_value IS NOT NULL;