package example.hugo.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import example.hugo.application.ArticleViewCountRepository;
import example.hugo.domain.ArticleViewCount;
import example.hugo.infra.entity.ArticleViewCountEntity;
import example.hugo.infra.entity.ArticleViewCountEntityRepository;
import example.hugo.infra.entity.QArticleViewCountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
class ArticleViewCountRepositoryImpl implements ArticleViewCountRepository {

    private final ArticleViewCountEntityRepository articleViewCountEntityRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ArticleViewCount> findById(Long articleId) {
        return articleViewCountEntityRepository.findById(articleId).map(ArticleViewCountEntity::toArticleViewCount);
    }

    @Override
    public void save(ArticleViewCount articleViewCount) {
        articleViewCountEntityRepository.save(ArticleViewCountEntity.from(articleViewCount));
    }

    @Transactional
    @Override
    public long increase(Long articleId) {
        QArticleViewCountEntity articleViewCountEntity = QArticleViewCountEntity.articleViewCountEntity;

        return queryFactory
            .update(articleViewCountEntity)
            .set(articleViewCountEntity.viewCount, articleViewCountEntity.viewCount.add(1))
            .where(articleViewCountEntity.articleId.eq(articleId))
            .execute();
    }

}
