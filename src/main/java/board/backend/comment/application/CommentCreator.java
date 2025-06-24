package board.backend.comment.application;

import board.backend.article.application.ArticleValidator;
import board.backend.comment.application.port.ArticleCommentCountRepository;
import board.backend.comment.application.port.CommentRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.Comment;
import board.backend.comment.domain.CommentNotFound;
import board.backend.common.cache.infra.CachedRepository;
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
    private final UserValidator userValidator;
    private final ArticleValidator articleValidator;
    private final CommentEventPublisher commentEventPublisher;

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
        articleCommentCountRepository.increase(articleId);

        // 게시글 댓글 수 캐시 삭제
        cachedArticleCommentCountRepository.delete(articleId);

        // 댓글 생성 이벤트 발행
        commentEventPublisher.publishEvent(newComment);

        return newComment;
    }

    private void checkCommentExistsOrThrow(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new CommentNotFound();
        }
    }

}
