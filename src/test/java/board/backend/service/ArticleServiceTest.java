package board.backend.service;

import board.backend.domain.Article;
import board.backend.domain.ArticleNotFound;
import board.backend.repository.ArticleRepository;
import board.backend.support.IdProvider;
import board.backend.support.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
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
    @DisplayName("lastArticleId가 null이면 첫 페이지 게시글을 조회한다")
    void readAll_without_lastArticleId_returns_first_page() {
        // given
        Long boardId = 1L;
        Long pageSize = 3L;
        Long lastArticleId = null;

        List<Article> articles = List.of(
            Article.create(3L, boardId, 100L, "제목1", "내용1", LocalDateTime.now()),
            Article.create(2L, boardId, 101L, "제목2", "내용2", LocalDateTime.now())
        );

        when(articleRepository.findAllByBoardId(boardId, pageSize)).thenReturn(articles);

        // when
        List<Article> result = articleService.readAll(boardId, pageSize, lastArticleId);

        // then
        assertThat(result).isEqualTo(articles);
    }

    @DisplayName("lastArticleId가 존재하면 해당 이후 페이지 게시글을 조회한다")
    void readAll_with_lastArticleId_returns_next_page() {
        // given
        Long boardId = 1L;
        Long pageSize = 3L;
        Long lastArticleId = 10L;

        List<Article> articles = List.of(
            Article.create(9L, boardId, 100L, "제목3", "내용3", LocalDateTime.now()),
            Article.create(8L, boardId, 101L, "제목4", "내용4", LocalDateTime.now())
        );

        when(articleRepository.findAllByBoardId(boardId, pageSize, lastArticleId)).thenReturn(articles);

        // when
        List<Article> result = articleService.readAll(boardId, pageSize, lastArticleId);

        // then
        assertThat(result).isEqualTo(articles);
    }

    @Test
    @DisplayName("게시글 조회에 성공한다")
    void read_success() {
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
    @DisplayName("게시글 수정에 성공한다")
    void update_success() {
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
    @DisplayName("게시글 삭제에 성공한다")
    void delete_success() {
        // given
        Long articleId = 1L;
        Article article = Article.create(articleId, 1L, 100L, "제목", "내용", LocalDateTime.now());

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        // when
        articleService.delete(articleId);
    }

    @Test
    @DisplayName("존재하지 않는 게시글이면 삭제하지 않는다")
    void delete_not_found_does_nothing() {
        // given
        Long articleId = 999L;
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // when
        articleService.delete(articleId);
    }

    @Test
    @DisplayName("게시글이 존재하면 예외를 던지지 않는다")
    void checkArticleExist_success() {
        // given
        Long articleId = 1L;
        when(articleRepository.existsById(articleId)).thenReturn(true);

        // when
        articleService.checkArticleExistOrThrow(articleId);
    }

    @Test
    @DisplayName("게시글이 존재하지 않으면 ArticleNotFound 예외를 던진다")
    void checkArticleExist_fail() {
        // given
        Long articleId = 999L;
        when(articleRepository.existsById(articleId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> articleService.checkArticleExistOrThrow(articleId))
            .isInstanceOf(ArticleNotFound.class);
    }

}