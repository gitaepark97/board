package example.hugo.application;

import example.hugo.domain.ArticleCommentCount;

import java.util.Optional;

public interface ArticleCommentCountRepository {

    Optional<ArticleCommentCount> findByArticleId(Long articleId);

    void save(ArticleCommentCount articleCommentCount);

    long increase(Long articleId);

    void decrease(Long articleId);

}
