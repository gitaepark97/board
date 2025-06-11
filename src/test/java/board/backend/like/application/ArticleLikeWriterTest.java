package board.backend.like.application;

import board.backend.article.application.ArticleReader;
import board.backend.common.support.TimeProvider;
import board.backend.like.domain.ArticleLike;
import board.backend.like.infra.ArticleLikeCountRepository;
import board.backend.like.infra.ArticleLikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleLikeWriterTest {

    private TimeProvider timeProvider;
    private ArticleLikeRepository articleLikeRepository;
    private ArticleLikeWriter articleLikeWriter;

    @BeforeEach
    void setUp() {
        timeProvider = mock(TimeProvider.class);
        articleLikeRepository = mock(ArticleLikeRepository.class);
        ArticleLikeCountRepository articleLikeCountRepository = mock(ArticleLikeCountRepository.class);
        ArticleReader articleReader = mock(ArticleReader.class);
        articleLikeWriter = new ArticleLikeWriter(timeProvider, articleLikeRepository, articleLikeCountRepository, articleReader);
    }

    @Test
    @DisplayName("게시글 좋아요에 성공한다")
    void like_success() {
        // given
        Long articleId = 1L;
        Long userId = 100L;
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);
        when(articleLikeRepository.existsByArticleIdAndUserId(articleId, userId)).thenReturn(false);
        when(timeProvider.now()).thenReturn(now);

        // when
        articleLikeWriter.like(articleId, userId);
    }

    @Test
    @DisplayName("이미 좋아요한 경우 아무 동작도 하지 않는다")
    void like_alreadyExists_doesNothing() {
        // given
        Long articleId = 1L;
        Long userId = 100L;
        when(articleLikeRepository.existsByArticleIdAndUserId(articleId, userId)).thenReturn(true);

        // when
        articleLikeWriter.like(articleId, userId);
    }

    @Test
    @DisplayName("좋아요 취소에 성공한다")
    void unlike_success() {
        // given
        Long articleId = 1L;
        Long userId = 100L;
        ArticleLike articleLike = mock(ArticleLike.class);
        when(articleLikeRepository.findByArticleIdAndUserId(articleId, userId)).thenReturn(Optional.of(articleLike));

        // when
        articleLikeWriter.unlike(articleId, userId);
    }

    @Test
    @DisplayName("좋아요가 없으면 취소하지 않는다")
    void unlike_notExists_doesNothing() {
        // given
        Long articleId = 1L;
        Long userId = 100L;
        when(articleLikeRepository.findByArticleIdAndUserId(articleId, userId)).thenReturn(Optional.empty());

        // when
        articleLikeWriter.unlike(articleId, userId);
    }

    @Test
    @DisplayName("게시글의 좋아요 데이터를 삭제한다")
    void deleteArticle_success() {
        // given
        Long articleId = 1L;

        // when
        articleLikeWriter.deleteArticle(articleId);
    }

}