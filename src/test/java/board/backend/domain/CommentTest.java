package board.backend.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class CommentTest {

    @Test
    @DisplayName("부모 ID가 null이면 댓글 생성 시 부모 ID에 자기 자신 id가 할당되고 isDeleted는 false이다")
    void create_with_null_parentId() {
        // given
        Long id = 1L;
        Long articleId = 10L;
        Long writerId = 100L;
        Long parentId = null;
        String content = "루트 댓글 내용";
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);

        // when
        Comment comment = Comment.create(id, articleId, writerId, parentId, content, now);

        // then
        assertThat(comment.getParentId()).isEqualTo(id);
        assertThat(comment.isRoot()).isTrue();
        assertThat(comment.getId()).isEqualTo(id);
        assertThat(comment.getArticleId()).isEqualTo(articleId);
        assertThat(comment.getWriterId()).isEqualTo(writerId);
        assertThat(comment.getContent()).isEqualTo(content);
        assertThat(comment.getCreatedAt()).isEqualTo(now);
        assertThat(comment.getIsDeleted()).isFalse();
    }

    @Test
    @DisplayName("부모 ID가 주어지면 댓글 생성 시 해당 값으로 할당되고, 루트 댓글이 아님을 판별할 수 있다")
    void create_with_nonNull_parentId() {
        // given
        Long id = 2L;
        Long articleId = 20L;
        Long writerId = 200L;
        // 루트 댓글이 아니라면 부모 ID가 자기 자신의 id와 달라야 함
        Long parentId = 1L;
        String content = "대댓글 내용";
        LocalDateTime now = LocalDateTime.of(2024, 1, 2, 11, 0);

        // when
        Comment comment = Comment.create(id, articleId, writerId, parentId, content, now);

        // then
        assertThat(comment.getParentId()).isEqualTo(parentId);
        assertThat(comment.isRoot()).isFalse();
        assertThat(comment.getId()).isEqualTo(id);
    }

    @Test
    @DisplayName("댓글 ID와 부모 ID가 같으면 루트 댓글로 판단한다")
    void is_root_success() {
        // given
        Long id = 5L;
        Long parentId = 5L;
        Comment comment = Comment.create(id, 10L, 100L, parentId, "루트 댓글", LocalDateTime.now());

        // expect
        assertThat(comment.isRoot()).isTrue();
    }

    @Test
    @DisplayName("댓글 ID와 부모 ID가 다르면 루트 댓글이 아니다")
    void is_not_root() {
        // given
        Long id = 6L;
        Long parentId = 5L;
        Comment comment = Comment.create(id, 10L, 100L, parentId, "대댓글", LocalDateTime.now());

        // expect
        assertThat(comment.isRoot()).isFalse();
    }

    @Test
    @DisplayName("댓글 삭제 시 isDeleted가 true로 변경된다")
    void delete_success() {
        // given
        Comment comment = Comment.create(1L, 10L, 100L, 1L, "삭제될 댓글", LocalDateTime.now());

        // when
        comment.delete();

        // then
        assertThat(comment.getIsDeleted()).isTrue();
    }

    @Test
    @DisplayName("작성자 본인이면 예외가 발생하지 않는다")
    void check_writer_success() {
        // given
        Long writerId = 1L;
        Comment comment = Comment.create(1L, 10L, writerId, 1L, "삭제될 댓글", LocalDateTime.now());

        // when & then
        comment.checkIsWriter(writerId);
    }

    @Test
    @DisplayName("작성자가 아니면 Forbidden 예외가 발생한다")
    void check_writer_fail() {
        // given
        Long writerId = 1L;
        Long anotherUserId = 2L;
        Comment comment = Comment.create(1L, 10L, writerId, 1L, "삭제될 댓글", LocalDateTime.now());

        // when & then
        assertThatThrownBy(() -> comment.checkIsWriter(anotherUserId))
            .isInstanceOf(Forbidden.class);
    }

}