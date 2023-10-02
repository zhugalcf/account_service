-- Write your sql migration here!
CREATE TABLE account (
	id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
	number varchar(20) UNIQUE check (LENGTH(number) BETWEEN 12 AND 20) not null,
	owner varchar(128) not null,
	type varchar(64) not null,
	currency varchar(3) not null,
	status varchar(64) not null,
	created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,
    closed_at timestamptz DEFAULT current_timestamp,
    version INTEGER not null
);

CREATE INDEX idx_unique_number ON account(number);