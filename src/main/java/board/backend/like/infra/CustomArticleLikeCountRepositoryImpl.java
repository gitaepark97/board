package board.backend.like.infra;

import board.backend.like.domain.QArticleLikeCount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CustomArticleLikeCountRepositoryImpl implements CustomArticleLikeCountRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public long increase(Long articleId) {
        QArticleLikeCount articleLikeCount = QArticleLikeCount.articleLikeCount;

        return queryFactory.update(articleLikeCount)
            .set(articleLikeCount.likeCount, articleLikeCount.likeCount.add(1))
            .where(articleLikeCount.articleId.eq(articleId))
            .execute();
    }

    @Override
    public void decrease(Long articleId) {
        QArticleLikeCount articleLikeCount = QArticleLikeCount.articleLikeCount;

        queryFactory.update(articleLikeCount)
            .set(articleLikeCount.likeCount, articleLikeCount.likeCount.subtract(1))
            .where(articleLikeCount.articleId.eq(articleId))
            .execute();
    }

}
