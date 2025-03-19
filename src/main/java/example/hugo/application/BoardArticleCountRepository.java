package example.hugo.application;

import example.hugo.domain.BoardArticleCount;

import java.util.Optional;

public interface BoardArticleCountRepository {

    Optional<BoardArticleCount> findByBoardId(Long boardId);

    void save(BoardArticleCount boardArticleCount);

    long increase(Long boardId);

    void decrease(Long boardId);

}
