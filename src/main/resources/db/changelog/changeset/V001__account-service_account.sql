-- Write your sql migration here!
CREATE TABLE users (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    username varchar(64) UNIQUE NOT NULL,
    request_type smallint NOT NULL,
    lock int,
    active boolean DEFAULT true NOT NULL,
    request_status smallint NOT NULL,
    details varchar(64),
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,
    version int
);