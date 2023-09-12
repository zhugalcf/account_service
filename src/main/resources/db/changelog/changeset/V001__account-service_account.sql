create table account
(
    id             bigserial primary key,
    account_number bigint not null unique
);

create table balance
(
    id                    bigserial primary key,
    account_id            bigint  not null unique references account (id),
    authorization_balance numeric not null,
    current_balance       numeric not null,
    created_at            timestamptz default current_timestamp,
    updated_at            timestamptz default current_timestamp,
    balance_version       bigint  not null
);