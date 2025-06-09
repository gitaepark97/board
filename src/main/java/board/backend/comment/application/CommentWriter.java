package board.backend.comment.application;

import board.backend.article.application.ArticleReader;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.Comment;
import board.backend.comment.domain.CommentNotFound;
import board.backend.comment.infra.ArticleCommentCountRepository;
import board.backend.comment.infra.CommentRepository;
import board.backend.common.support.IdProvider;
import board.backend.common.support.TimeProvider;
import board.backend.user.application.UserReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@Component
public class CommentWriter {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final CommentRepository commentRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;
    private final ArticleReader articleReader;
    private final UserReader userReader;

    @Transactional
    Comment create(Long articleId, Long userId, Long parentCommentId, String content) {
        // 게시글 존재 확인
        articleReader.checkArticleExistsOrThrow(articleId);

        // 회원 존재 확인
        userReader.checkUserExists(userId);

        // 부모 댓글 확인
        if (parentCommentId != null) {
            checkCommentExistsOrThrow(parentCommentId);
        }

        // 댓글 생성
        Comment newComment = Comment.create(idProvider.nextId(), articleId, userId, parentCommentId, content, timeProvider.now());
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
    void delete(Long commentId, Long userId) {
        commentRepository.findById(commentId).filter(not(Comment::getIsDeleted)).ifPresent(comment -> {
            comment.checkIsWriter(userId);

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

    @Transactional
    public void deleteArticle(Long articleId) {
        // 게시글 댓글 삭제
        commentRepository.deleteByArticleId(articleId);

        // 게시글 댓글 수 삭제
        articleCommentCountRepository.deleteById(articleId);
    }

    private void checkCommentExistsOrThrow(Long commentId) {
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
