create table article_like_count_snapshot
(
    snapshot_date date   not null,
    article_id    bigint not null,
    like_count    bigint not null,
    primary key (snapshot_date, article_id)
);

create table article_view_count_snapshot
(
    snapshot_date date   not null,
    article_id    bigint not null,
    view_count    bigint not null,
    primary key (snapshot_date, article_id)
);

create table article_comment_count_snapshot
(
    snapshot_date date   not null,
    article_id    bigint not null,
    comment_count bigint not null,
    primary key (snapshot_date, article_id)
);