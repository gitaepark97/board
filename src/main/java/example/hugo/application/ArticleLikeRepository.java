package example.hugo.application;

import example.hugo.domain.ArticleLike;

import java.util.Optional;

public interface ArticleLikeRepository {

    Optional<ArticleLike> findByArticleIdAndUserId(Long articleId, Long userId);
    
    void save(ArticleLike articleLike);

    void delete(ArticleLike articleLike);

}
