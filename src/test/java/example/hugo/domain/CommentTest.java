package example.hugo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommentTest {

    @Test
    @DisplayName("댓글 생성에 성공합니다.")
    void createComment_whenParentCommentIdIsNull() {
        // given
        Long commentId = 1L;
        String content = "댓글입니다.";
        Long articleId = 1L;
        Long writerId = 1L;
        LocalDateTime now = LocalDateTime.now();

        // when
        Comment result = Comment.create(commentId, content, null, articleId, writerId, now);

        // then
        assertThat(result.commentId()).isEqualTo(commentId);
        assertThat(result.content()).isEqualTo(content);
        assertThat(result.parentCommentId()).isEqualTo(commentId);
        assertThat(result.articleId()).isEqualTo(articleId);
        assertThat(result.writerId()).isEqualTo(writerId);
        assertThat(result.isDeleted()).isEqualTo(false);
        assertThat(result.createdAt()).isEqualTo(now);
        assertThat(result.deletedAt()).isNull();
    }

    @Test
    @DisplayName("대댓글 생성에 성공합니다.")
    void createComment_whenParentCommentIdIsExist() {
        // given
        Long commentId = 2L;
        String content = "댓글입니다.";
        Long parentCommentId = 1L;
        Long articleId = 1L;
        Long writerId = 1L;
        LocalDateTime now = LocalDateTime.now();

        // when
        Comment result = Comment.create(commentId, content, parentCommentId, articleId, writerId, now);

        // then
        assertThat(result.commentId()).isEqualTo(commentId);
        assertThat(result.content()).isEqualTo(content);
        assertThat(result.parentCommentId()).isEqualTo(parentCommentId);
        assertThat(result.articleId()).isEqualTo(articleId);
        assertThat(result.writerId()).isEqualTo(writerId);
        assertThat(result.isDeleted()).isEqualTo(false);
        assertThat(result.createdAt()).isEqualTo(now);
        assertThat(result.deletedAt()).isNull();
    }

    @Test
    @DisplayName("댓글 삭제에 성공합니다.")
    void deleteComment() {
        // given
        Comment existComment = Comment.create(1L, "댓글입니다.", null, 1L, 1L, LocalDateTime.now());
        LocalDateTime now = LocalDateTime.now();

        // when
        Comment result = existComment.delete(now);

        // then
        assertThat(result.commentId()).isEqualTo(existComment.commentId());
        assertThat(result.content()).isEqualTo(existComment.content());
        assertThat(result.parentCommentId()).isEqualTo(existComment.parentCommentId());
        assertThat(result.articleId()).isEqualTo(existComment.articleId());
        assertThat(result.writerId()).isEqualTo(existComment.writerId());
        assertThat(result.isDeleted()).isEqualTo(true);
        assertThat(result.createdAt()).isEqualTo(existComment.createdAt());
        assertThat(result.deletedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("대댓글 여부 확인에 성공합니다.")
    void isRootComment() {
        // given
        Comment rootComment = Comment.create(1L, "댓글입니다.", null, 1L, 1L, LocalDateTime.now());
        Comment comment = Comment.create(2L, "댓글입니다.", rootComment.commentId(), 1L, 1L, LocalDateTime.now());

        // when
        boolean result1 = rootComment.isRoot();
        boolean result2 = comment.isRoot();

        // then
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }

}