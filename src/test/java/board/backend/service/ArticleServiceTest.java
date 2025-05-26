package board.backend.service;

import board.backend.common.IdProvider;
import board.backend.common.TimeProvider;
import board.backend.domain.Article;
import board.backend.repository.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

}