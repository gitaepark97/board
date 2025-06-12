package board.backend.article.infra.jpa;

import board.backend.article.domain.Article;

import java.util.List;

interface CustomArticleRepository {

    boolean customExistsById(Long id);

    List<Article> findAllByBoardId(Long boardId, Long pageSize);

    List<Article> findAllByBoardId(Long boardId, Long pageSize, Long lastId);

}
