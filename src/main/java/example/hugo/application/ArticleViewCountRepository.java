package example.hugo.application;

import example.hugo.domain.ArticleViewCount;

import java.util.Optional;

public interface ArticleViewCountRepository {

    Optional<ArticleViewCount> findById(Long articleId);

    void save(ArticleViewCount articleViewCount);

    long increase(Long articleId);

}
