create table article_like
(
    article_id bigint   not null,
    user_id    bigint   not null,
    created_at datetime not null,
    primary key (article_id, user_id)
);

create table article_like_count
(
    article_id bigint not null primary key,
    like_count bigint not null,
    version    bigint not null
);