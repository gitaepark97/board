package board.backend.comment.application;

import board.backend.article.application.ArticleValidator;
import board.backend.comment.application.port.ArticleCommentCountRepository;
import board.backend.comment.application.port.CommentRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.Comment;
import board.backend.comment.domain.CommentNotFound;
import board.backend.common.event.EventPublisher;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.CommentCreatedEventPayload;
import board.backend.common.infra.CachedRepository;
import board.backend.common.support.IdProvider;
import board.backend.common.support.TimeProvider;
import board.backend.user.application.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CommentCreator {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final CachedRepository<ArticleCommentCount, Long> cachedArticleCommentCountRepository;
    private final CommentRepository commentRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;
    private final EventPublisher eventPublisher;
    private final UserValidator userValidator;
    private final ArticleValidator articleValidator;
    private final TodayCommentCountCalculator todayCommentCountCalculator;

    @Transactional
    Comment create(Long articleId, Long userId, Long parentCommentId, String content) {
        // 게시글 존재 확인
        articleValidator.checkArticleExistsOrThrow(articleId);

        // 사용자 존재 확인
        userValidator.checkUserExistsOrThrow(userId);

        // 부모 댓글 확인
        if (parentCommentId != null) {
            checkCommentExistsOrThrow(parentCommentId);
        }

        // 댓글 생성
        Comment newComment = Comment.create(idProvider.nextId(), articleId, userId, parentCommentId, content, timeProvider.now());
        // 댓글 저장
        commentRepository.save(newComment);

        // 게시글 댓글 수 증가
        ArticleCommentCount articleCommentCount = ArticleCommentCount.init(articleId);
        articleCommentCountRepository.increaseOrSave(articleCommentCount);

        // 게시글 댓글 수 캐시 삭제
        cachedArticleCommentCountRepository.delete(articleId);

        // 댓글 생성 이벤트 발행
        long todayCount = todayCommentCountCalculator.calculate(articleId);
        eventPublisher.publishEvent(EventType.COMMENT_CREATED, new CommentCreatedEventPayload(articleId, todayCount, newComment.createdAt()));

        return newComment;
    }

    private void checkCommentExistsOrThrow(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new CommentNotFound();
        }
    }


}
