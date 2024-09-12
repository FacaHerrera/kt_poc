CREATE TABLE IF NOT EXISTS task (
    id bigint not null,
    disabled boolean not null,
    title varchar not null,
    description varchar not null,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS state (
    id bigint not null,
    disabled boolean not null,
    date_from date,
    date_to date,
    state varchar not null,
    task_id bigint,
    primary key (id),
    CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES task(id) ON DELETE CASCADE
);

CREATE SEQUENCE task_sequence
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 1000000
    CACHE 1;

