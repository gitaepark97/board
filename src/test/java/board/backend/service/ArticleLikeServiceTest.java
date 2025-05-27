package board.backend.service;

import board.backend.repository.ArticleLikeRepository;
import board.backend.support.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleLikeServiceTest {

    private TimeProvider timeProvider;
    private ArticleLikeRepository articleLikeRepository;
    private ArticleLikeService articleLikeService;

    @BeforeEach
    void setUp() {
        timeProvider = mock(TimeProvider.class);
        articleLikeRepository = mock(ArticleLikeRepository.class);
        articleLikeService = new ArticleLikeService(timeProvider, articleLikeRepository);
    }

    @Test
    @DisplayName("게시글 좋아요에 성공한다")
    void like_success() {
        // given
        Long articleId = 1L;
        Long userId = 100L;
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        when(timeProvider.now()).thenReturn(now);

        // when
        articleLikeService.like(articleId, userId);
    }

}