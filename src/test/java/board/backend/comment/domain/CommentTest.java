package board.backend.comment.domain;

import board.backend.common.exception.Forbidden;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentTest {

    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    @Test
    @DisplayName("루트 댓글은 parentId와 id가 같다")
    void isRoot_true_whenParentIdEqualsId() {
        // given
        Comment comment = Comment.builder()
            .id(1L)
            .articleId(100L)
            .writerId(10L)
            .parentId(1L)
            .content("댓글")
            .createdAt(now)
            .isDeleted(false)
            .build();

        // when & then
        assertThat(comment.isRoot()).isTrue();
    }

    @Test
    @DisplayName("대댓글은 parentId와 id가 다르다")
    void isRoot_false_whenParentIdNotEqualsId() {
        // given
        Comment comment = Comment.builder()
            .id(2L)
            .articleId(100L)
            .writerId(10L)
            .parentId(1L)
            .content("대댓글")
            .createdAt(now)
            .isDeleted(false)
            .build();

        // when & then
        assertThat(comment.isRoot()).isFalse();
    }

    @Test
    @DisplayName("댓글을 삭제하면 isDeleted가 true로 바뀐다")
    void delete_success_setsIsDeletedTrue() {
        // given
        Comment comment = Comment.create(1L, 100L, 10L, null, "댓글", now);

        // when
        Comment deleted = comment.delete();

        // then
        assertThat(deleted.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("댓글 작성자가 맞으면 예외가 발생하지 않는다")
    void checkIsWriter_success_whenSameUserId() {
        // given
        Comment comment = Comment.create(1L, 100L, 10L, null, "댓글", now);

        // when & then
        comment.checkIsWriter(10L);
    }

    @Test
    @DisplayName("댓글 작성자가 아니면 예외가 발생한다")
    void checkIsWriter_fail_whenDifferentUserId_throwsException() {
        // given
        Comment comment = Comment.create(1L, 100L, 10L, null, "댓글", now);

        // when & then
        assertThatThrownBy(() -> comment.checkIsWriter(20L))
            .isInstanceOf(Forbidden.class);
    }

    @Test
    @DisplayName("create 시 parentId가 null이면 id로 설정된다")
    void create_setsParentIdToId_whenParentIdIsNull() {
        // when
        Comment comment = Comment.create(5L, 100L, 10L, null, "내용", now);

        // then
        assertThat(comment.parentId()).isEqualTo(comment.id());
        assertThat(comment.isRoot()).isTrue();
    }

    @Test
    @DisplayName("create 시 parentId가 있으면 그대로 설정된다")
    void create_setsParentId_whenGiven() {
        // when
        Comment comment = Comment.create(5L, 100L, 10L, 3L, "내용", now);

        // then
        assertThat(comment.parentId()).isEqualTo(3L);
        assertThat(comment.isRoot()).isFalse();
    }

}