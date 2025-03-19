package example.hugo.application;

import example.hugo.domain.ArticleCommentCount;
import example.hugo.domain.Comment;
import example.hugo.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@Component
class CommentWriter {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final CommentRepository commentRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;

    @Transactional
    Comment createComment(Long articleId, String content, Long parentCommentId, Long writerId) {
        // 부모 댓글 조회
        Optional<Comment> parentComment = findParent(parentCommentId);

        // 댓글 생성
        Comment newComment = Comment.create(idProvider.nextId(), content, parentComment.map(Comment::commentId)
            .orElse(null), articleId, writerId, timeProvider.now());
        commentRepository.save(newComment);

        // 게시글 댓글 수 변경
        long result = articleCommentCountRepository.increase(newComment.articleId());
        if (result == 0L) {
            articleCommentCountRepository.save(ArticleCommentCount.init(newComment.articleId()));
        }

        return newComment;
    }

    @Transactional
    void deleteComment(Long commentId) {
        // 댓글 조회
        Comment existComment = commentRepository.findById(commentId)
            .filter(not(Comment::isDeleted))
            .orElseThrow(ErrorCode.NOT_FOUND_COMMENT::toException);

        // 댓글 삭제
        if (hasChildren(existComment)) {
            Comment deletedComment = existComment.delete(timeProvider.now());
            commentRepository.save(deletedComment);
        } else {
            deleteComment(existComment);
        }
    }

    private void deleteComment(Comment comment) {
        // 게시글 삭제
        commentRepository.delete(comment);

        // 게시글 댓글 수 변경
        articleCommentCountRepository.decrease(comment.articleId());

        // 재귀
        if (!comment.isRoot()) {
            commentRepository.findById(comment.parentCommentId())
                .filter(Comment::isDeleted)
                .filter(not(this::hasChildren))
                .ifPresent(this::deleteComment);
        }
    }

    private boolean hasChildren(Comment comment) {
        return commentRepository.countByParentCommentId(comment.commentId(), 2L) == 2;
    }


    private Optional<Comment> findParent(Long parentCommentId) {
        if (parentCommentId == null) {
            return Optional.empty();
        }

        return Optional.of(commentRepository.findById(parentCommentId)
            .filter(not(Comment::isDeleted))
            .filter(Comment::isRoot)
            .orElseThrow(ErrorCode.NOT_FOUND_PARENT_COMMENT::toException)
        );
    }

}
