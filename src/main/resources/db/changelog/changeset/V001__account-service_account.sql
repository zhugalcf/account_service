CREATE TABLE balance (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    account_number character(16) NOT NULL,
    current_authorization_balance bigint,
    current_actual_balance bigint,
    created_at timestamptz DEFAULT current_timestamp NOT NULL,
    updated_at timestamptz DEFAULT current_timestamp NOT NULL,
    version bigint DEFAULT 0 NOT NULL,

    FOREIGN KEY (account_number) REFERENCES account(number) ON DELETE CASCADE
);
