CREATE TABLE request_task (
    id SERIAL PRIMARY KEY,
    handler VARCHAR(100) NOT NULL,
    status VARCHAR(255) NOT NULL,
    created_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ,
    version BIGINT,
    request_id UUID REFERENCES requests(idempotent_token)
);