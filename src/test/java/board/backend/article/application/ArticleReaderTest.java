package board.backend.article.application;

import board.backend.article.application.dto.ArticleWithWriterAndCounts;
import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.article.infra.ArticleCacheRepository;
import board.backend.article.infra.ArticleRepository;
import board.backend.comment.application.CommentReader;
import board.backend.like.application.ArticleLikeReader;
import board.backend.user.application.UserReader;
import board.backend.user.domain.User;
import board.backend.view.application.ArticleViewReader;
import board.backend.view.application.ArticleViewWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleReaderTest {

    private ArticleCacheRepository articleCacheRepository;
    private ArticleRepository articleRepository;
    private UserReader userReader;
    private ArticleLikeReader articleLikeReader;
    private ArticleViewReader articleViewReader;
    private CommentReader commentReader;
    private ArticleReader articleReader;

    @BeforeEach
    void setUp() {
        articleCacheRepository = mock(ArticleCacheRepository.class);
        articleRepository = mock(ArticleRepository.class);
        userReader = mock(UserReader.class);
        articleLikeReader = mock(ArticleLikeReader.class);
        articleViewReader = mock(ArticleViewReader.class);
        commentReader = mock(CommentReader.class);
        ArticleViewWriter articleViewWriter = mock(ArticleViewWriter.class);
        articleReader = new ArticleReader(articleCacheRepository, articleRepository, userReader, articleLikeReader, articleViewReader, commentReader, articleViewWriter);
    }

    @Test
    @DisplayName("게시글이 존재하면 예외가 발생하지 않는다")
    void checkArticleExists_success() {
        // given
        Long articleId = 1L;
        when(articleRepository.customExistsById(articleId)).thenReturn(true);

        // when
        articleReader.checkArticleExistsOrThrow(articleId);
    }

    @Test
    @DisplayName("게시글이 존재하지 않으면 예외가 발생한다")
    void checkArticleExists_fail() {
        // given
        Long articleId = 999L;
        when(articleRepository.customExistsById(articleId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> articleReader.checkArticleExistsOrThrow(articleId))
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
        Map<Long, User> writerMap = Map.of(
            1L, User.create(1L, "user1@email.com", "user1", LocalDateTime.now()),
            2L, User.create(2L, "user2@email.com", "user2", LocalDateTime.now())
        );
        when(articleRepository.findAllByBoardId(anyLong(), anyLong())).thenReturn(articles);
        when(userReader.readAll(anyList())).thenReturn(writerMap);
        when(articleLikeReader.count(anyList())).thenReturn(Map.of());
        when(articleViewReader.count(anyList())).thenReturn(Map.of());
        when(commentReader.count(anyList())).thenReturn(Map.of());

        // when
        List<ArticleWithWriterAndCounts> result = articleReader.readAll(boardId, pageSize, lastArticleId);

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
        Map<Long, User> writerMap = Map.of(
            100L, User.create(100L, "user100@email.com", "user100", LocalDateTime.now()),
            101L, User.create(101L, "user101@email.com", "user101", LocalDateTime.now())
        );
        when(articleRepository.findAllByBoardId(boardId, pageSize, lastArticleId)).thenReturn(articles);
        when(userReader.readAll(anyList())).thenReturn(writerMap);
        when(articleLikeReader.count(anyList())).thenReturn(Map.of());
        when(articleViewReader.count(anyList())).thenReturn(Map.of());
        when(commentReader.count(anyList())).thenReturn(Map.of());

        // when
        List<ArticleWithWriterAndCounts> result = articleReader.readAll(boardId, pageSize, lastArticleId);

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
        when(articleCacheRepository.get(articleId)).thenReturn(Optional.of(article));

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
        when(articleCacheRepository.get(articleId)).thenReturn(Optional.empty());
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
        when(articleCacheRepository.get(invalidId)).thenReturn(Optional.empty());
        when(articleRepository.findById(invalidId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> articleReader.read(invalidId, ip))
            .isInstanceOf(ArticleNotFound.class);
    }

}