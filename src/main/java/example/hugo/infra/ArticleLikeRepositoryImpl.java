package example.hugo.infra;

import example.hugo.application.ArticleLikeRepository;
import example.hugo.domain.ArticleLike;
import example.hugo.infra.entity.ArticleLikeEntity;
import example.hugo.infra.entity.ArticleLikeEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
class ArticleLikeRepositoryImpl implements ArticleLikeRepository {

    private final ArticleLikeEntityRepository articleLikeEntityRepository;

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

}
