package board.backend.hotArticle.application;

import board.backend.hotArticle.application.fake.FakeDailyArticleCountRepository;
import board.backend.hotArticle.application.fake.FakeHotArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class HotArticleDeleterTest {

    private FakeDailyArticleCountRepository dailyArticleLikeCountRepository;
    private FakeDailyArticleCountRepository dailyArticleViewCountRepository;
    private FakeDailyArticleCountRepository dailyArticleCommentCountRepository;
    private FakeHotArticleRepository hotArticleRepository;
    private HotArticleDeleter hotArticleDeleter;

    @BeforeEach
    void setUp() {
        dailyArticleLikeCountRepository = new FakeDailyArticleCountRepository();
        dailyArticleViewCountRepository = new FakeDailyArticleCountRepository();
        dailyArticleCommentCountRepository = new FakeDailyArticleCountRepository();
        hotArticleRepository = new FakeHotArticleRepository();
        hotArticleDeleter = new HotArticleDeleter(
            dailyArticleLikeCountRepository,
            dailyArticleViewCountRepository,
            dailyArticleCommentCountRepository,
            hotArticleRepository
        );
    }

    @Test
    @DisplayName("핫 게시글 관련 데이터를 모두 삭제한다")
    void delete_success_deletesAllRelatedData() {
        // given
        Long articleId = 1L;
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);
        dailyArticleLikeCountRepository.save(articleId, 1L, now, Duration.ofHours(1));
        dailyArticleViewCountRepository.save(articleId, 10L, now, Duration.ofHours(1));
        dailyArticleCommentCountRepository.save(articleId, 1L, now, Duration.ofHours(1));
        hotArticleRepository.save(articleId, now, 100L, 10L, Duration.ofHours(1));

        // when
        hotArticleDeleter.delete(articleId);

        // then
        assertThat(dailyArticleLikeCountRepository.read(articleId, now)).isEqualTo(0L);
        assertThat(dailyArticleViewCountRepository.read(articleId, now)).isEqualTo(0L);
        assertThat(dailyArticleCommentCountRepository.read(articleId, now)).isEqualTo(0L);
        assertThat(hotArticleRepository.readAll("2024-01-01")).doesNotContain(articleId);
    }

}