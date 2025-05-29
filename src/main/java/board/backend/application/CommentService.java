package board.backend.application;

import board.backend.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentWriter commentWriter;

    public Comment create(Long articleId, Long writerId, Long parentCommentId, String content) {
        return commentWriter.create(articleId, writerId, parentCommentId, content);
    }

    public void delete(Long commentId) {
        commentWriter.delete(commentId);
    }

}
