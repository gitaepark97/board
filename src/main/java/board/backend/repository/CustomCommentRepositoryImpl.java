package board.backend.repository;

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

        List<Comment> limitedComments = queryFactory
            .select(comment)
            .from(comment)
            .where(
                comment.articleId.eq(articleId),
                comment.parentId.eq(parentId)
            )
            .limit(limit)
            .fetch();

        return limitedComments.size();
    }

}
