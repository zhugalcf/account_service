CREATE TABLE request (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    uuid VARCHAR(255) NOT NULL,
    user_id bigint NOT NULL,
    request_type smallint NOT NULL,
    lock_value VARCHAR(255) NOT NULL,
    is_open BOOLEAN NOT NULL,
    input_data TEXT,
    status smallint NOT NULL,
    status_details VARCHAR,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,
    version bigint
);

CREATE INDEX user_index ON request (user_id);
CREATE UNIQUE INDEX open_request_index ON request (lock_value) WHERE is_open;