CREATE TABLE free_account_numbers
(
    id             bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    account_type   smallint           NOT NULL,
    account_number VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE account_numbers_sequence
(
    id           bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    account_type smallint NOT NULL,
    current      bigint   NOT NULL,
    version      bigint DEFAULT 0
);