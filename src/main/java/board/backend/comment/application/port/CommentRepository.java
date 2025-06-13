package board.backend.comment.application.port;

import board.backend.comment.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    boolean existsById(Long id);

    int countBy(Long articleId, Long parentId, Integer limit);

    Optional<Comment> findById(Long id);

    List<Comment> findAllById(Long articleId, Long pageSize);

    List<Comment> findAllById(Long articleId, Long pageSize, Long lastParentId, Long lastId);

    void save(Comment comment);

    void delete(Comment comment);

    void deleteByByArticleId(Long articleId);

}
