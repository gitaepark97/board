package board.backend.comment.infra;

import board.backend.comment.domain.QArticleCommentCount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CustomArticleCommentCountRepositoryImpl implements CustomArticleCommentCountRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public void decrease(Long articleId) {
        QArticleCommentCount articleCommentCount = QArticleCommentCount.articleCommentCount;

        queryFactory.update(articleCommentCount)
            .set(articleCommentCount.commentCount, articleCommentCount.commentCount.subtract(1))
            .where(articleCommentCount.articleId.eq(articleId))
            .execute();
    }

}
