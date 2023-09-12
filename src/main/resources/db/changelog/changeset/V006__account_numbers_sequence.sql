CREATE TABLE account_numbers_sequence
(
    type VARCHAR(20) NOT NULL UNIQUE ,
    current BIGINT NOT NULL
);