package board.backend.infra;

import board.backend.domain.Article;

import java.util.List;

interface CustomArticleRepository {

    boolean customExistsById(Long id);

    List<Article> findAllByBoardId(Long boardId, Long pageSize);

    List<Article> findAllByBoardId(Long boardId, Long pageSize, Long lastId);

}
