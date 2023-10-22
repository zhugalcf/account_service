CREATE TABLE requests (
  idempotent_token UUID PRIMARY KEY,
  user_id BIGINT,
  type VARCHAR(255),
  lock VARCHAR(255),
  is_open BOOLEAN,
  input JSONB,
  status VARCHAR(255),
  details VARCHAR(255),
  created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
  opt_lock INT
);

CREATE INDEX idx_user_id ON requests (user_id);
CREATE UNIQUE INDEX uniq_lock_user_id ON requests (lock, user_id) WHERE is_open = true;