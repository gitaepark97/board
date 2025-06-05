package board.backend.application;

import board.backend.domain.Article;
import board.backend.infra.ArticleRepository;
import board.backend.support.IdProvider;
import board.backend.support.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleWriterTest {

    private IdProvider idProvider;
    private TimeProvider timeProvider;
    private ArticleRepository articleRepository;
    private ArticleWriter articleWriter;

    @BeforeEach
    void setUp() {
        idProvider = mock(IdProvider.class);
        timeProvider = mock(TimeProvider.class);
        articleRepository = mock(ArticleRepository.class);

        articleWriter = new ArticleWriter(idProvider, timeProvider, articleRepository);
    }


    @Test
    @DisplayName("게시글 생성에 성공한다")
    void create_success() {
        // given
        Long generatedId = 1L;
        Long boardId = 10L;
        Long writerId = 100L;
        String title = "제목입니다";
        String content = "내용입니다";
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        when(idProvider.nextId()).thenReturn(generatedId);
        when(timeProvider.now()).thenReturn(now);

        // when
        Article article = articleWriter.create(boardId, writerId, title, content);

        // then
        assertThat(article.getId()).isEqualTo(generatedId);
        assertThat(article.getBoardId()).isEqualTo(boardId);
        assertThat(article.getWriterId()).isEqualTo(writerId);
        assertThat(article.getTitle()).isEqualTo(title);
        assertThat(article.getContent()).isEqualTo(content);
        assertThat(article.getCreatedAt()).isEqualTo(now);
        assertThat(article.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("게시글 수정에 성공한다")
    void update_success() {
        // given
        Long userId = 1L;
        Long articleId = 1L;
        String newTitle = "수정된 제목";
        String newContent = "수정된 내용";
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 2, 12, 0);
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(Article.create(articleId, 1L, userId, "원래 제목", "원래 내용", createdAt)));
        when(timeProvider.now()).thenReturn(updatedAt);

        // when
        Article updated = articleWriter.update(articleId, userId, newTitle, newContent);

        // then
        assertThat(updated.getTitle()).isEqualTo(newTitle);
        assertThat(updated.getContent()).isEqualTo(newContent);
        assertThat(updated.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(updated.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("게시글 삭제에 성공한다")
    void delete_success() {
        // given
        Long userId = 1L;
        Long articleId = 1L;
        Article article = Article.create(articleId, 1L, userId, "제목", "내용", LocalDateTime.now());

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        // when
        articleWriter.delete(articleId, userId);
    }

    @Test
    @DisplayName("존재하지 않는 게시글이면 삭제하지 않는다")
    void delete_notFoundDoesNothing() {
        // given
        Long userId = 1L;
        Long articleId = 999L;
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // when
        articleWriter.delete(articleId, userId);
    }

}