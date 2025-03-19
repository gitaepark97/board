package example.hugo.application;

import example.hugo.domain.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    boolean existsById(Long articleId);

    Optional<Article> findById(Long articleId);

    List<Article> findAllByBoardId(Long boardId, Long limit);

    List<Article> findAllByBoardId(Long boardId, Long limit, Long lastArticleId);

    void save(Article article);

    void deleteById(Long articleId);

}
