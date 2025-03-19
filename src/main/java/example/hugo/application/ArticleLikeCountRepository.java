package example.hugo.application;

import example.hugo.domain.ArticleLikeCount;

import java.util.Optional;

public interface ArticleLikeCountRepository {

    Optional<ArticleLikeCount> findByArticleId(Long articleId);

    void save(ArticleLikeCount articleLikeCount);

    long increase(Long articleId);

    void decrease(Long articleId);

}
