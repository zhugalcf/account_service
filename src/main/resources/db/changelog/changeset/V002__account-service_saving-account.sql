create TABLE savings_account(
id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY unique,
account_id bigint NOT NULL,

last_interest_date timestamptz DEFAULT current_timestamp,
version bigint NOT NULL,
created_at timestamptz DEFAULT current_timestamp,
updated_at timestamptz DEFAULT current_timestamp
);
