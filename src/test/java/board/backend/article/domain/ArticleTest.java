package board.backend.article.domain;


import board.backend.common.exception.Forbidden;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class ArticleTest {

    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);

    @Test
    @DisplayName("게시글을 생성할 수 있다")
    void create_success_returnsArticle() {
        // given
        Long id = 1L;
        Long boardId = 100L;
        Long writerId = 10L;
        String title = "제목";
        String content = "내용";

        // when
        Article article = Article.create(id, boardId, writerId, title, content, now);

        // then
        assertThat(article.id()).isEqualTo(id);
        assertThat(article.boardId()).isEqualTo(boardId);
        assertThat(article.writerId()).isEqualTo(writerId);
        assertThat(article.title()).isEqualTo(title);
        assertThat(article.content()).isEqualTo(content);
        assertThat(article.createdAt()).isEqualTo(now);
        assertThat(article.updatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("작성자가 게시글을 수정할 수 있다")
    void update_success_whenWriter_updatesArticle() {
        // given
        Article article = Article.create(1L, 100L, 10L, "제목", "내용", now);
        String newTitle = "수정된 제목";
        String newContent = "수정된 내용";
        LocalDateTime newTime = now.plusHours(1);

        // when
        Article updated = article.update(10L, newTitle, newContent, newTime);

        // then
        assertThat(updated.title()).isEqualTo(newTitle);
        assertThat(updated.content()).isEqualTo(newContent);
        assertThat(updated.updatedAt()).isEqualTo(newTime);
    }

    @Test
    @DisplayName("작성자가 아닌 경우 게시글 수정 시 예외가 발생한다")
    void update_fail_whenNotWriter_throwsForbidden() {
        // given
        Article article = Article.create(1L, 100L, 10L, "제목", "내용", now);

        // when & then
        assertThatThrownBy(() -> article.update(99L, "t", "c", now))
            .isInstanceOf(Forbidden.class);
    }

    @Test
    @DisplayName("작성자인 경우 checkIsWriter가 통과된다")
    void checkIsWriter_success_whenSameUserId_doesNotThrow() {
        // given
        Article article = Article.create(1L, 100L, 10L, "제목", "내용", now);

        // when & then
        assertThatCode(() -> article.checkIsWriter(10L))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("작성자가 아닌 경우 checkIsWriter가 예외를 발생시킨다")
    void checkIsWriter_fail_whenDifferentUserId_throwsForbidden() {
        // given
        Article article = Article.create(1L, 100L, 10L, "제목", "내용", now);

        // when & then
        assertThatThrownBy(() -> article.checkIsWriter(11L))
            .isInstanceOf(Forbidden.class);
    }

}