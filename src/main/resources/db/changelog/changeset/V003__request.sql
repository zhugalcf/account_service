CREATE TABLE request (
    idempotent_token UUID PRIMARY KEY,
    user_id bigint NOT NULL,
    request_type smallint NOT NULL,
    lock_value bigint NOT NULL,
    active boolean NOT NULL,
    input_data JSONB,
    request_status varchar(128) NOT NULL,
    details varchar(128),
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,
    version bigint NOT NULL
);
