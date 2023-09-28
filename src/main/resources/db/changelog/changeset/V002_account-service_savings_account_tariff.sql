create table savings_account
(
    id             bigint primary key generated always as identity unique,
    number         varchar(20) unique not null,
    account_id     bigint,
    tariff_history varchar,
    amount         numeric(17, 5) default 0,
    time_accrual   timestamptz    default current_timestamp,
    created_at     timestamptz    default current_timestamp,
    updated_at     timestamptz    default current_timestamp,
    version        int,

    CONSTRAINT fk_account_id foreign key (account_id) references account (id) on delete cascade
);

create table tariff
(
    id              bigint primary key generated always as identity unique,
    type_tariff     varchar(64) unique not null,
    bet             numeric(3, 1)      not null,
    betting_history varchar
);