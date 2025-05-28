package board.backend.repository;

import board.backend.domain.Article;

import java.util.List;

interface CustomArticleRepository {

    List<Article> findAllByBoardId(Long boardId, Long pageSize);

    List<Article> findAllByBoardId(Long writerId, Long pageSize, Long lastArticleId);

}
