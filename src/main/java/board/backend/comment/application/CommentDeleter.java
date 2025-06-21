package board.backend.comment.application;

import board.backend.comment.application.port.ArticleCommentCountRepository;
import board.backend.comment.application.port.CommentRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.Comment;
import board.backend.common.event.EventPublisher;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.CommentDeletedEventPayload;
import board.backend.common.infra.CachedRepository;
import board.backend.common.support.TimeProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@Component
class CommentDeleter {

    private final TimeProvider timeProvider;
    private final CachedRepository<ArticleCommentCount, Long> cachedArticleCommentCountRepository;
    private final CommentRepository commentRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;
    private final EventPublisher eventPublisher;
    private final TodayCommentCountCalculator todayCommentCountCalculator;

    @Transactional
    Optional<Long> delete(Long commentId, Long userId) {
        return commentRepository.findById(commentId)
            .filter(not(Comment::isDeleted))
            .map(comment -> {
                comment.checkIsWriter(userId);

                if (hasChildren(comment)) {
                    // 댓글 삭제
                    Comment deletedComment = comment.delete();
                    // 댓글 저장
                    commentRepository.save(deletedComment);
                    return deletedComment.id();
                } else {
                    delete(comment);

                    // 댓글 삭제 이벤트 발행
                    long todayCount = todayCommentCountCalculator.calculate(comment.articleId());
                    eventPublisher.publishEvent(EventType.COMMENT_DELETED, new CommentDeletedEventPayload(comment.articleId(), todayCount, timeProvider.now()));

                    return comment.articleId();
                }
            });
    }

    @Transactional
    void deleteArticle(Long articleId) {
        // 게시글 댓글 삭제
        commentRepository.deleteByByArticleId(articleId);

        // 게시글 댓글 수 삭제
        articleCommentCountRepository.deleteById(articleId);

        // 게시글 댓글 수 캐시 삭제
        cachedArticleCommentCountRepository.delete(articleId);
    }

    private boolean hasChildren(Comment comment) {
        return commentRepository.countBy(comment.articleId(), comment.id(), 2) == 2;
    }

    private void delete(Comment comment) {
        // 게시글 댓글 수 캐시 삭제
        cachedArticleCommentCountRepository.delete(comment.articleId());

        commentRepository.delete(comment);
        articleCommentCountRepository.decrease(comment.articleId());
        if (!comment.isRoot()) {
            // 부모 댓글 삭제
            commentRepository.findById(comment.parentId())
                .filter(Comment::isDeleted)
                .filter(not(this::hasChildren))
                .ifPresent(this::delete);
        }
    }

}
