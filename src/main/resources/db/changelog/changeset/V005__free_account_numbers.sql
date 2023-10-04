CREATE TABLE free_account_numbers
(
    type VARCHAR(20) NOT NULL,
    account_number VARCHAR(20) NOT NULL,
    PRIMARY KEY (type, account_number)
);