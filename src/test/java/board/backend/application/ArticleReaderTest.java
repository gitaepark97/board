package board.backend.application;

import board.backend.domain.Article;
import board.backend.domain.ArticleNotFound;
import board.backend.repository.ArticleRepository;
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

class ArticleReaderTest {

    private ArticleRepository articleRepository;
    private ArticleReader articleReader;

    @BeforeEach
    void setUp() {
        articleRepository = mock(ArticleRepository.class);

        articleReader = new ArticleReader(articleRepository);
    }

    @Test
    @DisplayName("lastArticleId가 null이면 첫 페이지 게시글을 조회한다")
    void readAll_withoutLastArticleIdReturnsFirstPage() {
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
        List<Article> result = articleReader.readAll(boardId, pageSize, lastArticleId);

        // then
        assertThat(result).isEqualTo(articles);
    }

    @DisplayName("lastArticleId가 존재하면 해당 이후 페이지 게시글을 조회한다")
    void readAll_withLastArticleIdReturnsNextPage() {
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
        List<Article> result = articleReader.readAll(boardId, pageSize, lastArticleId);

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
        Article result = articleReader.read(articleId);

        // then
        assertThat(result).isEqualTo(article);
    }

    @Test
    @DisplayName("게시글이 존재하지 않으면 예외가 발생한다")
    void read_failWhenArticleNotFound() {
        // given
        Long invalidId = 999L;
        when(articleRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> articleReader.read(invalidId))
            .isInstanceOf(ArticleNotFound.class);
    }

    @Test
    @DisplayName("게시글이 존재하면 예외를 던지지 않는다")
    void checkArticleExist_success() {
        // given
        Long articleId = 1L;
        when(articleRepository.existsById(articleId)).thenReturn(true);

        // when
        articleReader.checkArticleExistOrThrow(articleId);
    }

    @Test
    @DisplayName("게시글이 존재하지 않으면 ArticleNotFound 예외를 던진다")
    void checkArticleExist_fail() {
        // given
        Long articleId = 999L;
        when(articleRepository.existsById(articleId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> articleReader.checkArticleExistOrThrow(articleId))
            .isInstanceOf(ArticleNotFound.class);
    }

}