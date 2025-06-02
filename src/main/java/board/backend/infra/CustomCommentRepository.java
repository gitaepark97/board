package board.backend.infra;

import board.backend.domain.Comment;

import java.util.List;

interface CustomCommentRepository {

    int countBy(Long articleId, Long parentId, Integer limit);

    List<Comment> findAllByArticleId(Long articleId, Long pageSize);

    List<Comment> findAllByArticleId(Long articleId, Long pageSize, Long lastParentId, Long lastId);

}
