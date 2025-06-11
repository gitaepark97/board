package board.backend.article.application;

import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.article.infra.ArticleRepository;
import board.backend.comment.application.CommentWriter;
import board.backend.common.infra.CacheRepository;
import board.backend.common.support.IdProvider;
import board.backend.common.support.TimeProvider;
import board.backend.like.application.ArticleLikeWriter;
import board.backend.view.application.ArticleViewWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
        CacheRepository<Article, Long> articleCacheRepository = mock(CacheRepository.class);
        articleRepository = mock(ArticleRepository.class);
        ArticleLikeWriter articleLikeWriter = mock(ArticleLikeWriter.class);
        ArticleViewWriter articleViewWriter = mock(ArticleViewWriter.class);
        CommentWriter commentWriter = mock(CommentWriter.class);
        articleWriter = new ArticleWriter(idProvider, timeProvider, articleCacheRepository, articleRepository, articleLikeWriter, articleViewWriter, commentWriter);
    }


    @Test
    @DisplayName("게시글 생성에 성공한다")
    void create_success() {
        // given
        Long boardId = 1L;
        Long writerId = 10L;
        String title = "제목";
        String content = "내용";
        Long newArticleId = 100L;
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        when(idProvider.nextId()).thenReturn(newArticleId);
        when(timeProvider.now()).thenReturn(now);

        // when
        Article result = articleWriter.create(boardId, writerId, title, content);

        // then
        assertThat(result.getId()).isEqualTo(newArticleId);
        assertThat(result.getBoardId()).isEqualTo(boardId);
        assertThat(result.getWriterId()).isEqualTo(writerId);
    }

    @Test
    @DisplayName("게시글 수정에 성공한다")
    void update_success() {
        // given
        Long articleId = 1L;
        Long writerId = 10L;
        String title = "수정된 제목";
        String content = "수정된 내용";
        Article article = mock(Article.class);
        Article updatedArticle = mock(Article.class);

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));
        when(timeProvider.now()).thenReturn(LocalDateTime.now());
        when(article.update(writerId, title, content, timeProvider.now())).thenReturn(updatedArticle);

        // when
        Article result = articleWriter.update(articleId, writerId, title, content);

        // then
        assertThat(result).isEqualTo(updatedArticle);
    }

    @Test
    @DisplayName("게시글 수정 시 게시글이 존재하지 않으면 예외가 발생한다")
    void update_failWhenNotFound() {
        // given
        Long articleId = 999L;
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> articleWriter.update(articleId, 1L, "제목", "내용"))
            .isInstanceOf(ArticleNotFound.class);
    }

    @Test
    @DisplayName("게시글 삭제에 성공한다")
    void delete_success() {
        // given
        Long articleId = 1L;
        Long writerId = 10L;
        Article article = mock(Article.class);

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        // when
        articleWriter.delete(articleId, writerId);
    }

}