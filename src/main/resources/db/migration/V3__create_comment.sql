create table comment
(
    id         bigint        not null primary key,
    article_id bigint        not null,
    writer_id  bigint        not null,
    parent_id  bigint,
    content    varchar(3000) not null,
    created_at datetime      not null,
    is_deleted bool          not null
);

create index idx_article_id_parent_comment_id_comment_id on comment (article_id asc, parent_id asc, id asc);
