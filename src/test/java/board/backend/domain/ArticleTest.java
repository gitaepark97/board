package board.backend.domain;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ArticleTest {

    @Test
    void create_Article_정상생성() {
        // given
        Long id = 1L;
        Long boardId = 10L;
        Long writerId = 100L;
        String title = "첫 번째 게시글";
        String content = "내용입니다.";

        // when
        Article article = Article.create(id, boardId, writerId, title, content);

        // then
        assertThat(article.getId()).isEqualTo(id);
        assertThat(article.getBoardId()).isEqualTo(boardId);
        assertThat(article.getWriterId()).isEqualTo(writerId);
        assertThat(article.getTitle()).isEqualTo(title);
        assertThat(article.getContent()).isEqualTo(content);
        assertThat(article.getCreatedAt()).isNotNull();
        assertThat(article.getUpdatedAt()).isNotNull();
        assertThat(article.getCreatedAt()).isEqualTo(article.getUpdatedAt());
    }

    @Test
    void update_Article_제목과내용수정() {
        // given
        Article article = Article.create(1L, 10L, 100L, "이전 제목", "이전 내용");

        String newTitle = "수정된 제목";
        String newContent = "수정된 내용";
        LocalDateTime beforeUpdate = article.getUpdatedAt();

        // when
        Article updatedArticle = article.update(newTitle, newContent);

        // then
        assertThat(updatedArticle.getTitle()).isEqualTo(newTitle);
        assertThat(updatedArticle.getContent()).isEqualTo(newContent);
        assertThat(updatedArticle.getUpdatedAt()).isAfter(beforeUpdate);
    }

}