create table article_like
(
    article_id bigint   not null,
    user_id    bigint   not null,
    created_at datetime not null,
    primary key (article_id, user_id)
);