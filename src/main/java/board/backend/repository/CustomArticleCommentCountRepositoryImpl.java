package board.backend.repository;

import board.backend.domain.QArticleCommentCount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CustomArticleCommentCountRepositoryImpl implements CustomArticleCommentCountRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public long increase(Long articleId) {
        QArticleCommentCount articleCommentCount = QArticleCommentCount.articleCommentCount;

        return queryFactory.update(articleCommentCount)
            .set(articleCommentCount.commentCount, articleCommentCount.commentCount.add(1))
            .where(articleCommentCount.articleId.eq(articleId))
            .execute();
    }

    @Override
    public void decrease(Long articleId) {
        QArticleCommentCount articleCommentCount = QArticleCommentCount.articleCommentCount;

        queryFactory.update(articleCommentCount)
            .set(articleCommentCount.commentCount, articleCommentCount.commentCount.subtract(1))
            .where(articleCommentCount.articleId.eq(articleId))
            .execute();
    }

}
