package board.backend.domain;


import board.backend.support.Forbidden;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ArticleTest {

    @Test
    @DisplayName("Article 생성에 성공한다")
    void create_success() {
        // given
        Long id = 1L;
        Long boardId = 10L;
        Long writerId = 100L;
        String title = "제목";
        String content = "내용";
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);

        // when
        Article article = Article.create(id, boardId, writerId, title, content, now);

        // then
        assertThat(article.getId()).isEqualTo(id);
        assertThat(article.getBoardId()).isEqualTo(boardId);
        assertThat(article.getWriterId()).isEqualTo(writerId);
        assertThat(article.getTitle()).isEqualTo(title);
        assertThat(article.getContent()).isEqualTo(content);
        assertThat(article.getCreatedAt()).isEqualTo(now);
        assertThat(article.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Article 수정 시 제목과 내용이 변경되고 updatedAt이 갱신된다")
    void update_success() {
        // given
        Long userId = 1L;
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        Article article = Article.create(1L, 10L, userId, "이전 제목", "이전 내용", createdAt);

        String newTitle = "수정된 제목";
        String newContent = "수정된 내용";
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 2, 12, 0);

        // when
        Article updated = article.update(userId, newTitle, newContent, updatedAt);

        // then
        assertThat(updated.getTitle()).isEqualTo(newTitle);
        assertThat(updated.getContent()).isEqualTo(newContent);
        assertThat(updated.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(updated.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("작성자 본인이면 예외가 발생하지 않는다")
    void check_writer_success() {
        // given
        Long writerId = 1L;
        Article article = Article.create(1L, 1L, writerId, "제목", "내용", null); // 예시

        // when & then
        article.checkIsWriter(writerId);
    }

    @Test
    @DisplayName("작성자가 아니면 Forbidden 예외가 발생한다")
    void check_writer_fail() {
        // given
        Long writerId = 1L;
        Long anotherUserId = 2L;
        Article article = Article.create(1L, 1L, writerId, "제목", "내용", null); // 예시

        // when & then
        assertThatThrownBy(() -> article.checkIsWriter(anotherUserId))
            .isInstanceOf(Forbidden.class);
    }

}