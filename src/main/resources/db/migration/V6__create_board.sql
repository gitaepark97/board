create table board
(
    id         bigint      not null primary key,
    title      varchar(20) not null unique,
    created_at datetime    not null
);
