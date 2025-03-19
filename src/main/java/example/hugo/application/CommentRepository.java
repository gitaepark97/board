package example.hugo.application;

import example.hugo.domain.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {

    Long countByParentCommentId(Long parentCommentId, Long limit);

    Optional<Comment> findById(Long commentId);

    List<Comment> findAllByArticleId(Long articleId, Long limit);

    List<Comment> findAllByArticleId(Long articleId, Long limit, Long lastParentCommentId, Long lastCommentId);

    void save(Comment comment);

    void delete(Comment comment);

}
