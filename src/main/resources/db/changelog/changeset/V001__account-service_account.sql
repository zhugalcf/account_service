CREATE TABLE account (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    number varchar(20) UNIQUE NOT NULL,
    user_id bigint,
    project_id bigint,
    type smallint NOT NULL,
    currency varchar(3) NOT NULL,
    status smallint DEFAULT 0,
    created_at timestamptz DEFAULT current_timestamp,
    updated_at timestamptz DEFAULT current_timestamp,
    closed_at timestamptz DEFAULT current_timestamp,
    version INT,

    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_project_id FOREIGN KEY (project_id) REFERENCES project (id) ON DELETE CASCADE,
    CONSTRAINT Check_MinimumLength CHECK (LENGTH(number) >= 12)
);