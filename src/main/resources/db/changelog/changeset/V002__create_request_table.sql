CREATE TABLE request (
    uuid varchar PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id bigint not null,
    type INTEGER not null,
    lock bigint not null,
    flag boolean not null,
    input varchar not null,
    status INTEGER not null,
    additionally varchar(256),
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,
    version INTEGER not null,
    FOREIGN KEY (user_id) REFERENCES User (id)
);

CREATE INDEX index_user ON request(user_id);

CREATE INDEX index_lock ON request(lock);