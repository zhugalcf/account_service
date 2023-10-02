CREATE INDEX idempotent_key
ON request(idempotent_key)

CREATE UNIQUE INDEX lock_request
ON request(lock_value)

CREATE INDEX user_request
ON request(input_data)