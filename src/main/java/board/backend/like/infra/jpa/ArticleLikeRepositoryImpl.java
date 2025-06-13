package board.backend.like.infra.jpa;

import board.backend.like.application.port.ArticleLikeRepository;
import board.backend.like.domain.ArticleLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
class ArticleLikeRepositoryImpl implements ArticleLikeRepository {

    private final JPAQueryFactory queryFactory;
    private final ArticleLikeEntityRepository articleLikeEntityRepository;

    @Override
    public boolean existsByArticleIdAndUserId(Long articleId, Long userId) {
        QArticleLikeEntity articleLikeEntity = QArticleLikeEntity.articleLikeEntity;

        Integer result = queryFactory
            .selectOne()
            .from(articleLikeEntity)
            .where(
                articleLikeEntity.articleId.eq(articleId),
                articleLikeEntity.userId.eq(userId)
            )
            .fetchFirst();

        return result != null;
    }

    @Override
    public Optional<ArticleLike> findByArticleIdAndUserId(Long articleId, Long userId) {
        return articleLikeEntityRepository.findByArticleIdAndUserId(articleId, userId)
            .map(ArticleLikeEntity::toArticleLike);
    }

    @Override
    public void save(ArticleLike articleLike) {
        articleLikeEntityRepository.save(ArticleLikeEntity.from(articleLike));
    }

    @Override
    public void delete(ArticleLike articleLike) {
        articleLikeEntityRepository.delete(ArticleLikeEntity.from(articleLike));
    }

    @Override
    public void deleteByArticleId(Long articleId) {
        articleLikeEntityRepository.deleteByArticleId(articleId);
    }

}
