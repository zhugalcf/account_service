CREATE TABLE request (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    idempotency_key varchar(256) NOT NULL,
    username varchar(64) UNIQUE NOT NULL,
    request_type smallint NOT NULL,
    lock_value bigint NOT NULL,
    active boolean NOT NULL,
    text varchar(128),
    request_status varchar(128) NOT NULL,
    details varchar(128),
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,
    version bigint NOT NULL
);
