CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE request (
                         request_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         user_id BIGINT NOT NULL,
                         request_type SMALLINT DEFAULT 0 NOT NULL,
                         lock_value BIGINT NOT NULL,
                         is_open BOOLEAN NOT NULL,
                         input_data JSONB,
                         request_status SMALLINT DEFAULT 0 NOT NULL,
                         status_details TEXT,
                         created_at TIMESTAMP NOT NULL,
                         last_modified TIMESTAMP NOT NULL,
                         version BIGINT NOT NULL
);

CREATE INDEX idx_request_user_id ON request (user_id);
CREATE INDEX idx_request_request_status ON request (request_status);
CREATE UNIQUE INDEX idx_lock_value_open_request ON request (lock_value) WHERE is_open = true;
