package board.backend.comment.application;

import board.backend.comment.application.dto.CommentWithWriter;
import board.backend.comment.application.port.CommentRepository;
import board.backend.comment.domain.Comment;
import board.backend.user.application.UserReader;
import board.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@NamedInterface
@RequiredArgsConstructor
@Component
public class CommentReader {
    
    private final CommentRepository commentRepository;
    private final UserReader userReader;

    public List<CommentWithWriter> readAll(
        Long articleId,
        Long pageSize,
        Long lastParentCommentId,
        Long lastCommentId
    ) {
        List<Comment> comments = lastParentCommentId == null || lastCommentId == null ?
            commentRepository.findAllById(articleId, pageSize) :
            commentRepository.findAllById(articleId, pageSize, lastParentCommentId, lastCommentId);

        List<Long> writerIds = comments.stream().map(Comment::writerId).toList();

        // 작성자 조회
        Map<Long, User> writerMap = userReader.readAll(writerIds);

        return comments.stream()
            .map(comment -> CommentWithWriter.of(comment, writerMap.get(comment.writerId())))
            .toList();
    }

}
