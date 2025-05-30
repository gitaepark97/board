package board.backend.application;

import board.backend.domain.ArticleCommentCount;
import board.backend.domain.Comment;
import board.backend.domain.CommentNotFound;
import board.backend.repository.ArticleCommentCountRepository;
import board.backend.repository.CommentRepository;
import board.backend.support.IdProvider;
import board.backend.support.TimeProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@Component
class CommentWriter {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final CommentRepository commentRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;

    @Transactional
    Comment create(Long articleId, Long writerId, Long parentCommentId, String content) {
        // 부모 댓글 확인
        if (parentCommentId != null) {
            checkCommentExistOrThrow(parentCommentId);
        }

        // 댓글 생성
        Comment newComment = Comment.create(idProvider.nextId(), articleId, writerId, parentCommentId, content, timeProvider.now());
        // 댓글 저장
        commentRepository.save(newComment);

        // 게시글 댓글 수 증가
        long result = articleCommentCountRepository.increase(articleId);
        if (result == 0) {
            articleCommentCountRepository.save(ArticleCommentCount.init(articleId));
        }

        return newComment;
    }

    @Transactional
    void delete(Long commentId) {
        commentRepository.findById(commentId).filter(not(Comment::getIsDeleted)).ifPresent(comment -> {
            if (hasChildren(comment)) {
                // 댓글 삭제
                Comment deletedComment = comment.delete();
                // 댓글 저장
                commentRepository.save(deletedComment);
            } else {
                delete(comment);
            }
        });
    }

    private void checkCommentExistOrThrow(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new CommentNotFound();
        }
    }

    private boolean hasChildren(Comment comment) {
        return commentRepository.countBy(comment.getArticleId(), comment.getId(), 2) == 2;
    }

    private void delete(Comment comment) {
        commentRepository.delete(comment);
        articleCommentCountRepository.decrease(comment.getArticleId());
        if (!comment.isRoot()) {
            // 부모 댓글 삭제
            commentRepository.findById(comment.getParentId())
                .filter(Comment::getIsDeleted)
                .filter(not(this::hasChildren))
                .ifPresent(this::delete);
        }
    }

}
