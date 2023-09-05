CREATE TABLE free_account_numbers
(
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    account_type smallint NOT NULL,
    account_number VARCHAR(20) CHECK (LENGTH(account_number) >= 12) NOT NULL,
    CONSTRAINT uc_account_type_account_number UNIQUE (account_type, account_number)
);
CREATE UNIQUE INDEX account_type_number_idx ON free_account_numbers (account_type, account_number);

CREATE TABLE account_numbers_sequence
(
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    account_type smallint NOT NULL,
    current_count BIGINT NOT NULL DEFAULT 0
);
CREATE UNIQUE INDEX account_numbers_sequence_ind ON account_numbers_sequence (account_type, current_count);
