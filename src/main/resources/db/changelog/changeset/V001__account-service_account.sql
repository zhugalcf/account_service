CREATE TABLE requests (
  id BIGINT PRIMARY KEY NOT NULL GENERATED ALWAYS AS IDENTITY,
  idempotent_token VARCHAR(255),
  user_id BIGINT,
  type VARCHAR(255),
  lock VARCHAR(255),
  is_open BOOLEAN,
  input VARCHAR(255),
  status VARCHAR(255),
  details VARCHAR(255),
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  opt_lock INT
);

CREATE INDEX idx_idempotent_token on requests(idempotent_token);
CREATE INDEX idx_user_id ON requests (user_id);
CREATE UNIQUE INDEX uniq_opened_lock ON requests (lock) WHERE is_open = true and lock IS NOT NULL;
