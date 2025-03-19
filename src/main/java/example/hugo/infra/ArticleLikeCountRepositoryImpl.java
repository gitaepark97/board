package example.hugo.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import example.hugo.application.ArticleLikeCountRepository;
import example.hugo.domain.ArticleLikeCount;
import example.hugo.infra.entity.ArticleLikeCountEntity;
import example.hugo.infra.entity.ArticleLikeCountEntityRepository;
import example.hugo.infra.entity.QArticleLikeCountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
class ArticleLikeCountRepositoryImpl implements ArticleLikeCountRepository {

    private final ArticleLikeCountEntityRepository articleLikeCountEntityRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ArticleLikeCount> findById(Long articleId) {
        return articleLikeCountEntityRepository.findById(articleId).map(ArticleLikeCountEntity::toArticleLikeCount);
    }

    @Override
    public void save(ArticleLikeCount articleLikeCount) {
        articleLikeCountEntityRepository.save(ArticleLikeCountEntity.from(articleLikeCount));
    }

    @Override
    public long increase(Long articleId) {
        QArticleLikeCountEntity articleLikeCountEntity = QArticleLikeCountEntity.articleLikeCountEntity;

        return queryFactory
            .update(articleLikeCountEntity)
            .set(articleLikeCountEntity.likeCount, articleLikeCountEntity.likeCount.add(1))
            .where(articleLikeCountEntity.articleId.eq(articleId))
            .execute();
    }

    @Override
    public void decrease(Long articleId) {
        QArticleLikeCountEntity articleLikeCountEntity = QArticleLikeCountEntity.articleLikeCountEntity;

        queryFactory
            .update(articleLikeCountEntity)
            .set(articleLikeCountEntity.likeCount, articleLikeCountEntity.likeCount.subtract(1))
            .where(articleLikeCountEntity.articleId.eq(articleId))
            .execute();
    }

}
