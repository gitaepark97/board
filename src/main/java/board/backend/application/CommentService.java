package board.backend.application;

import board.backend.application.dto.CommentWithWriter;
import board.backend.domain.Comment;
import board.backend.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentReader commentReader;
    private final UserReader userReader;
    private final CommentWriter commentWriter;

    public List<CommentWithWriter> readAll(
        Long articleId,
        Long pageSize,
        Long lastParentCommentId,
        Long lastCommentId
    ) {
        // 댓글 목록 조회
        List<Comment> comments = commentReader.readAll(articleId, pageSize, lastParentCommentId, lastCommentId);
        List<Long> writerIds = comments.stream().map(Comment::getWriterId).toList();

        // 작성자 조회
        Map<Long, User> writerMap = userReader.readAll(writerIds);

        return comments.stream()
            .map(comment -> CommentWithWriter.of(comment, writerMap.get(comment.getWriterId())))
            .toList();
    }

    public Comment create(Long articleId, Long writerId, Long parentCommentId, String content) {
        return commentWriter.create(articleId, writerId, parentCommentId, content);
    }

    public void delete(Long commentId, Long userId) {
        commentWriter.delete(commentId, userId);
    }

}
