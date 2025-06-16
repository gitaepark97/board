package board.backend.article.application.port;

import board.backend.article.domain.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository {

    boolean existsById(Long id);

    Optional<Article> findById(Long id);

    List<Article> findAllById(List<Long> ids);

    List<Article> findAllByBoardId(Long boardId, Long pageSize);

    List<Article> findAllByBoardId(Long boardId, Long pageSize, Long lastId);

    void save(Article article);

    void delete(Article article);

}
