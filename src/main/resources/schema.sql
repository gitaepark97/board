CREATE TABLE article
(
    article_id bigint        not null primary key,
    title      varchar(100)  not null,
    content    varchar(3000) not null,
    board_id   bigint        not null,
    writer_id  bigint        not null,
    created_at datetime      not null,
    updated_at datetime      not null
);

create table board_article_count
(
    board_id      bigint not null primary key,
    article_count bigint not null
);

create table comment
(
    comment_id        bigint        not null primary key,
    content           varchar(3000) not null,
    article_id        bigint        not null,
    parent_comment_id bigint,
    writer_id         bigint        not null,
    is_deleted        bool          not null,
    created_at        datetime      not null,
    deleted_at        datetime
);

create table article_comment_count
(
    article_id    bigint not null primary key,
    comment_count bigint not null
);