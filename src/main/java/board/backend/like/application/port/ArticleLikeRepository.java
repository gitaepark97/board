package board.backend.like.application.port;

import board.backend.like.domain.ArticleLike;

import java.util.Optional;

public interface ArticleLikeRepository {

    boolean existsByArticleIdAndUserId(Long articleId, Long userId);

    Optional<ArticleLike> findByArticleIdAndUserId(Long articleId, Long userId);

    void save(ArticleLike articleLike);

    void delete(ArticleLike articleLike);

    void deleteByArticleId(Long articleId);

}
