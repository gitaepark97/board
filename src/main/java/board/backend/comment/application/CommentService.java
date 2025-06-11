package board.backend.comment.application;

import board.backend.comment.application.dto.CommentWithWriter;
import board.backend.comment.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentReader commentReader;
    private final CommentWriter commentWriter;

    @Cacheable(value = "commentList::article", key = "#articleId", condition = "#pageSize.equals(10L) && #lastParentCommentId == null && #lastCommentId == null")
    public List<CommentWithWriter> readAll(
        Long articleId,
        Long pageSize,
        Long lastParentCommentId,
        Long lastCommentId
    ) {
        return commentReader.readAll(articleId, pageSize, lastParentCommentId, lastCommentId);
    }

    public Comment create(Long articleId, Long userId, Long parentCommentId, String content) {
        return commentWriter.create(articleId, userId, parentCommentId, content);
    }

    public void delete(Long commentId, Long userId) {
        commentWriter.delete(commentId, userId);
    }

}
