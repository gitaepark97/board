package board.backend.infra;

import board.backend.domain.QArticleViewCount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CustomArticleViewCountRepositoryImpl implements CustomArticleViewCountRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public long increase(Long articleId) {
        QArticleViewCount articleViewCount = QArticleViewCount.articleViewCount;

        return queryFactory.update(articleViewCount)
            .set(articleViewCount.viewCount, articleViewCount.viewCount.add(1))
            .where(articleViewCount.articleId.eq(articleId))
            .execute();
    }
    
}
