package example.hugo.application;

import example.hugo.domain.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    Optional<Article> findById(Long articleId);

    List<Article> findByBoardId(Long boardId, Long limit);

    List<Article> findByBoardId(Long boardId, Long limit, Long lastArticleId);

    void save(Article article);

    void deleteById(Long articleId);

}
