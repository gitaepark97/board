package board.backend.infra;

import board.backend.domain.Comment;
import board.backend.domain.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class CustomCommentRepositoryImpl implements CustomCommentRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public int countBy(Long articleId, Long parentId, Integer limit) {
        QComment comment = QComment.comment;

        List<Long> limitedComments = queryFactory
            .select(comment.id)
            .from(comment)
            .where(
                comment.articleId.eq(articleId),
                comment.parentId.eq(parentId)
            )
            .limit(limit)
            .fetch();

        return limitedComments.size();
    }

    @Override
    public List<Comment> findAllById(Long articleId, Long pageSize) {
        QComment comment = QComment.comment;

        return queryFactory
            .selectFrom(comment)
            .where(
                comment.articleId.eq(articleId)
            )
            .orderBy(comment.parentId.asc(), comment.id.asc())
            .limit(pageSize)
            .fetch();
    }

    @Override
    public List<Comment> findAllById(
        Long articleId,
        Long pageSize,
        Long lastParentId,
        Long lastId
    ) {
        QComment comment = QComment.comment;

        return queryFactory
            .selectFrom(comment)
            .where(
                comment.articleId.eq(articleId),
                comment.parentId.gt(lastParentId)
                    .or(comment.parentId.eq(lastParentId).and(comment.id.gt(lastId)))
            )
            .orderBy(comment.parentId.asc(), comment.id.asc())
            .limit(pageSize)
            .fetch();
    }

}
