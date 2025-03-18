package example.hugo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class ArticleTest {

    @Test
    @DisplayName("게시글 생성에 성공합니다.")
    void createArticle() {
        // given
        Long articleId = 1L;
        String title = "제목";
        String content = "게시글 내용입니다.";
        Long boardId = 1L;
        Long writerId = 1L;
        LocalDateTime now = LocalDateTime.now();

        // when
        Article result = Article.create(articleId, title, content, boardId, writerId, now);

        // then
        assertThat(result.articleId()).isEqualTo(articleId);
        assertThat(result.title()).isEqualTo(title);
        assertThat(result.content()).isEqualTo(content);
        assertThat(result.boardId()).isEqualTo(boardId);
        assertThat(result.writerId()).isEqualTo(writerId);
        assertThat(result.createdAt()).isEqualTo(now);
        assertThat(result.updatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("게시글 수정에 성공합니다.")
    void updateArticle() {
        // given
        Article existArticle = Article.create(1L, "제목", "게시글 내용입니다.", 1L, 1L, LocalDateTime.now());
        String title = "수정된 제목";
        String content = "수정된 게시글 내용입니다.";
        LocalDateTime now = LocalDateTime.now();

        // when
        Article result = existArticle.update(title, content, now);

        // then
        assertThat(result.articleId()).isEqualTo(existArticle.articleId());
        assertThat(result.title()).isNotEqualTo(existArticle.title());
        assertThat(result.title()).isEqualTo(title);
        assertThat(result.title()).isNotEqualTo(existArticle.content());
        assertThat(result.content()).isEqualTo(content);
        assertThat(result.boardId()).isEqualTo(existArticle.boardId());
        assertThat(result.writerId()).isEqualTo(existArticle.writerId());
        assertThat(result.createdAt()).isEqualTo(existArticle.createdAt());
        assertThat(result.updatedAt()).isNotEqualTo(existArticle.updatedAt());
        assertThat(result.updatedAt()).isEqualTo(now);
    }

}