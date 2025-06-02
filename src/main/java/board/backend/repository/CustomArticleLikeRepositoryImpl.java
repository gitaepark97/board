package board.backend.repository;

import board.backend.domain.QArticleLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CustomArticleLikeRepositoryImpl implements CustomArticleLikeRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsByArticleIdAndUserId(Long articleId, Long userId) {
        QArticleLike articleLike = QArticleLike.articleLike;

        Integer result = queryFactory
            .selectOne()
            .from(articleLike)
            .where(
                articleLike.articleId.eq(articleId),
                articleLike.userId.eq(userId)
            )
            .fetchFirst();

        return result != null;
    }

}
