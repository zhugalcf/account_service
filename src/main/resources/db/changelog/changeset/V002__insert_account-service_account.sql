INSERT INTO owner (owner_type, owner_id) VALUES ('USER', 1);
INSERT INTO owner (owner_type, owner_id) VALUES ('PROJECT', 2);

INSERT INTO currency (currency_name) VALUES ('USD');
INSERT INTO currency (currency_name) VALUES ('EUR');
INSERT INTO currency (currency_name) VALUES ('RUB');

INSERT INTO account (account_number, owner_id, type, currency_id, status)
VALUES ('1234567890123', 1, 0, 1, 0);
INSERT INTO account (account_number, owner_id, type, currency_id, status)
VALUES ('12345678901230', 1, 0, 1, 0);