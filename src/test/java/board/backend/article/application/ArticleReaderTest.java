package board.backend.article.application;

import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.article.infra.ArticleRepository;
import board.backend.common.infra.CachedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleReaderTest {

    private CachedRepository<Article, Long> articleCachedRepository;
    private ArticleRepository articleRepository;
    private ArticleReader articleReader;

    @BeforeEach
    void setUp() {
        articleCachedRepository = mock(CachedRepository.class);
        articleRepository = mock(ArticleRepository.class);
        ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
        articleReader = new ArticleReader(articleCachedRepository, articleRepository, applicationEventPublisher);
    }

    @Test
    @DisplayName("회원이 캐시에 존재하면 예외 없이 통과한다")
    void checkArticleExistsOrThrow_whenCacheHit_shouldPass() {
        // given
        Long articleId = 1L;
        when(articleCachedRepository.findByKey(articleId)).thenReturn(Optional.of(mock(Article.class)));

        // when
        articleReader.checkArticleExistsOrThrow(articleId);
    }

    @Test
    @DisplayName("회원이 캐시에 없지만 DB에 존재하면 예외가 발생하지 않는다")
    void checkArticleExistsOrThrow_successByDB() {
        // given
        Long articleId = 1L;
        when(articleCachedRepository.findByKey(articleId)).thenReturn(Optional.empty());
        when(articleRepository.customExistsById(articleId)).thenReturn(true);

        // when
        articleReader.checkArticleExistsOrThrow(articleId);
    }

    @Test
    @DisplayName("회원이 캐시에 없고 DB에는 존재하면 예외 없이 통과한다")
    void checkArticleExistsOrThrow_whenCacheMissAndDbHit_shouldPass() {
        // given
        Long articleId = 1L;
        when(articleCachedRepository.findByKey(articleId)).thenReturn(Optional.empty());
        when(articleRepository.customExistsById(articleId)).thenReturn(true);

        // when
        articleReader.checkArticleExistsOrThrow(articleId);
    }

    @Test
    @DisplayName("회원이 캐시와 DB에 모두 없으면 예외가 발생한다")
    void checkArticleExistsOrThrow_whenCacheAndDbMiss_shouldThrow() {
        // given
        Long articleId = 1L;
        when(articleCachedRepository.findByKey(articleId)).thenReturn(Optional.empty());
        when(articleRepository.customExistsById(articleId)).thenReturn(false);

        // when & then
        Assertions.assertThatThrownBy(() -> articleReader.checkArticleExistsOrThrow(articleId))
            .isInstanceOf(ArticleNotFound.class);
    }

    @Test
    @DisplayName("lastArticleId가 null이면 첫 페이지 게시글 목록을 조회한다")
    void readAll_firstPage_success() {
        // given
        Long boardId = 1L;
        Long pageSize = 3L;
        Long lastArticleId = null;
        List<Article> articles = List.of(
            Article.create(3L, boardId, 1L, "제목1", "내용1", LocalDateTime.now()),
            Article.create(2L, boardId, 2L, "제목2", "내용2", LocalDateTime.now())
        );
        when(articleRepository.findAllByBoardId(anyLong(), anyLong())).thenReturn(articles);

        // when
        List<Article> result = articleReader.readAll(boardId, pageSize, lastArticleId);

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("lastArticleId가 존재하면 다음 페이지 게시글 목록을 조회한다")
    void readAll_nextPage_success() {
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
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("캐싱된 게시글 단건 조회에 성공한다")
    void read_success_withCache() {
        // given
        Long articleId = 1L;
        Article article = Article.create(
            articleId, 10L, 100L, "조회 제목", "조회 내용",
            LocalDateTime.of(2024, 1, 1, 10, 0)
        );
        String ip = "0:0:0:0";
        when(articleCachedRepository.findByKey(articleId)).thenReturn(Optional.of(article));

        // when
        Article result = articleReader.read(articleId, ip);

        // then
        assertThat(result).isEqualTo(article);
    }

    @Test
    @DisplayName("게시글 단건 조회에 성공한다")
    void read_success() {
        // given
        Long articleId = 1L;
        Article article = Article.create(
            articleId, 10L, 100L, "조회 제목", "조회 내용",
            LocalDateTime.of(2024, 1, 1, 10, 0)
        );
        String ip = "0:0:0:0";
        when(articleCachedRepository.findByKey(articleId)).thenReturn(Optional.empty());
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        // when
        Article result = articleReader.read(articleId, ip);

        // then
        assertThat(result).isEqualTo(article);
    }

    @Test
    @DisplayName("게시글 단건 조회 시 존재하지 않으면 예외가 발생한다")
    void read_failWhenNotFound() {
        // given
        Long invalidId = 999L;
        String ip = "0:0:0:0";
        when(articleCachedRepository.findByKey(invalidId)).thenReturn(Optional.empty());
        when(articleRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> articleReader.read(invalidId, ip))
            .isInstanceOf(ArticleNotFound.class);
    }

}