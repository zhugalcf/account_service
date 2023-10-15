CREATE TABLE request (
    uuid UUID PRIMARY KEY not null,
    user_id bigint not null,
    type varchar(64) not null,
    lock bigint not null,
    is_open boolean not null,
    input json not null,
    status INTEGER not null,
    additionally varchar(256),
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,
    version bigint not null,

    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT constraint_lock UNIQUE (lock)
);

CREATE INDEX index_user ON request(user_id);

CREATE UNIQUE INDEX index_lock ON request(lock)
    WHERE lock IS NOT NULL;