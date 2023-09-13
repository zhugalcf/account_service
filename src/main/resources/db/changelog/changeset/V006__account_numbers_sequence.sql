CREATE TABLE account_numbers_sequence
(
    type VARCHAR(20) NOT NULL UNIQUE PRIMARY KEY,
    current BIGINT NOT NULL
);