package board.backend.application;

import board.backend.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentReader commentReader;
    private final CommentWriter commentWriter;

    public List<Comment> readAll(Long articleId, Long pageSize, Long lastParentCommentId, Long lastCommentId) {
        return commentReader.readAll(articleId, pageSize, lastParentCommentId, lastCommentId);
    }

    public Comment create(Long articleId, Long writerId, Long parentCommentId, String content) {
        return commentWriter.create(articleId, writerId, parentCommentId, content);
    }

    public void delete(Long commentId, Long userId) {
        commentWriter.delete(commentId, userId);
    }

}
