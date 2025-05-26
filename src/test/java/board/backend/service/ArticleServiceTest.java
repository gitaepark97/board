package board.backend.service;

import board.backend.common.IdProvider;
import board.backend.common.TimeProvider;
import board.backend.domain.Article;
import board.backend.domain.ArticleNotFound;
import board.backend.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleServiceTest {

    private IdProvider idProvider;
    private TimeProvider timeProvider;
    private ArticleRepository articleRepository;
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        idProvider = mock(IdProvider.class);
        timeProvider = mock(TimeProvider.class);
        articleRepository = mock(ArticleRepository.class);

        articleService = new ArticleService(idProvider, timeProvider, articleRepository);
    }

    @Test
    void create_정상동작() {
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
        Article article = articleService.create(boardId, writerId, title, content);

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
    void read_정상조회() {
        // given
        Long articleId = 1L;
        Article article = Article.create(
            articleId, 10L, 100L, "조회 제목", "조회 내용",
            LocalDateTime.of(2024, 1, 1, 10, 0)
        );
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        // when
        Article result = articleService.read(articleId);

        // then
        assertThat(result).isEqualTo(article);
    }

    @Test
    void read_존재하지않을때_예외() {
        // given
        Long invalidId = 999L;
        when(articleRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> articleService.read(invalidId))
            .isInstanceOf(ArticleNotFound.class);
    }

    @Test
    void update_정상동작() {
        // given
        Long articleId = 1L;
        String newTitle = "수정된 제목";
        String newContent = "수정된 내용";
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 2, 12, 0);
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(Article.create(articleId, 1L, 100L, "원래 제목", "원래 내용", createdAt)));
        when(timeProvider.now()).thenReturn(updatedAt);

        // when
        Article updated = articleService.update(articleId, newTitle, newContent);

        // then
        assertThat(updated.getTitle()).isEqualTo(newTitle);
        assertThat(updated.getContent()).isEqualTo(newContent);
        assertThat(updated.getUpdatedAt()).isEqualTo(updatedAt);
        assertThat(updated.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void delete_정상동작() {
        // given
        Long articleId = 1L;

        // when
        articleService.delete(articleId);
    }

}