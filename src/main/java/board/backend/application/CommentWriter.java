package board.backend.application;

import board.backend.domain.Comment;
import board.backend.repository.CommentRepository;
import board.backend.support.IdProvider;
import board.backend.support.TimeProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CommentWriter {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final CommentRepository commentRepository;

    @Transactional
    Comment create(Long articleId, Long writerId, Long parentCommentId, String content) {
        // 댓글 생성
        Comment newComment = Comment.create(idProvider.nextId(), articleId, writerId, parentCommentId, content, timeProvider.now());
        // 댓글 저장
        commentRepository.save(newComment);

        return newComment;
    }

}
