create table user
(
    id         bigint       not null primary key,
    email      varchar(100) not null unique,
    nickname   varchar(20),
    created_at datetime     not null,
    updated_at datetime     not null
);

create table login_info
(
    id           bigint         not null primary key,
    user_id      bigint         not null,
    login_method enum ('EMAIL') not null,
    login_key    varchar(255)   not null unique,
    password     varchar(255),
    created_at   datetime       not null,
    updated_at   datetime       not null,
    unique (login_method, login_key)
);