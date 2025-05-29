package board.backend.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommentTest {

    @Test
    @DisplayName("댓글 생성에 성공한다")
    void create_success() {
        // given
        Long id = 1L;
        Long articleId = 10L;
        Long writerId = 100L;
        Long parentId = 1L;
        String content = "댓글 내용";
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);

        // when
        Comment comment = Comment.create(id, articleId, writerId, parentId, content, now);

        // then
        assertThat(comment.getId()).isEqualTo(id);
        assertThat(comment.getArticleId()).isEqualTo(articleId);
        assertThat(comment.getWriterId()).isEqualTo(writerId);
        assertThat(comment.getParentId()).isEqualTo(parentId);
        assertThat(comment.getContent()).isEqualTo(content);
        assertThat(comment.getCreatedAt()).isEqualTo(now);
        assertThat(comment.getIsDeleted()).isFalse();
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

}