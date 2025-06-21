package board.backend.view.application;

import board.backend.common.event.EventType;
import board.backend.common.event.fake.FakeEventPublisher;
import board.backend.common.event.payload.ArticleViewedEventPayload;
import board.backend.common.support.fake.FakeTimeProvider;
import board.backend.view.application.fake.FakeArticleViewCountBackupRepository;
import board.backend.view.application.fake.FakeArticleViewCountRepository;
import board.backend.view.application.fake.FakeArticleViewCountSnapshotRepository;
import board.backend.view.application.fake.FakeArticleViewDistributedLockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleViewManagerTest {

    private final Long articleId = 1L;
    private final String ip = "1.1.1.1";

    private FakeTimeProvider timeProvider;
    private FakeArticleViewCountRepository articleViewCountRepository;
    private FakeArticleViewCountBackupRepository articleViewCountBackupRepository;
    private FakeArticleViewDistributedLockRepository articleViewDistributedLockRepository;
    private FakeEventPublisher eventPublisher;
    private ArticleViewManager articleViewManager;

    @BeforeEach
    void setUp() {
        timeProvider = new FakeTimeProvider(LocalDateTime.of(2025, 6, 19, 10, 0));
        articleViewCountRepository = new FakeArticleViewCountRepository();
        articleViewCountBackupRepository = new FakeArticleViewCountBackupRepository();
        articleViewDistributedLockRepository = new FakeArticleViewDistributedLockRepository();
        FakeArticleViewCountSnapshotRepository articleViewCountSnapshotRepository = new FakeArticleViewCountSnapshotRepository();
        eventPublisher = new FakeEventPublisher();
        articleViewManager = new ArticleViewManager(
            timeProvider,
            articleViewCountRepository,
            articleViewCountBackupRepository,
            articleViewDistributedLockRepository,
            eventPublisher,
            new TodayViewCountCalculator(timeProvider, articleViewCountSnapshotRepository)
        );
    }

    @Test
    @DisplayName("락 획득 실패 시 아무 동작도 하지 않는다")
    void increaseCount_락획득실패_조회수변화없음() {
        // given
        articleViewDistributedLockRepository.lock(articleId, ip, Duration.ofMinutes(1));

        // when
        articleViewManager.increaseCount(articleId, ip);

        // then
        assertThat(articleViewCountRepository.findById(articleId)).isEqualTo(0L);
        assertThat(articleViewCountBackupRepository.findById(articleId)).isEmpty();
        assertThat(eventPublisher.getPublishedEvents()).isEmpty();
    }

    @Test
    @DisplayName("조회수를 1 증가시킨다")
    void increaseCount_정상호출_조회수1증가() {
        // when
        articleViewManager.increaseCount(articleId, ip);

        // then
        assertThat(articleViewCountRepository.findById(articleId)).isEqualTo(1L);
    }

    @Test
    @DisplayName("조회수가 10 미만이면 백업되지 않는다")
    void increaseCount_10미만_백업없음() {
        // when
        for (int i = 0; i < 9; i++) {
            articleViewManager.increaseCount(articleId, "1.1.1." + i);
        }

        // then
        assertThat(articleViewCountBackupRepository.findById(articleId)).isEmpty();
    }

    @Test
    @DisplayName("조회수가 10의 배수면 백업 및 이벤트 발행")
    void increaseCount_10배수_백업및이벤트발행() {
        // when
        for (int i = 0; i < 10; i++) {
            articleViewManager.increaseCount(articleId, "1.1.1." + i);
        }

        // then
        var articleViewCount = articleViewCountBackupRepository.findById(articleId);
        assertThat(articleViewCount).isPresent();
        assertThat(articleViewCount.get().viewCount()).isEqualTo(10);
        assertThat(eventPublisher.getPublishedEvents())
            .containsExactly(
                new FakeEventPublisher.PublishedEvent(
                    EventType.ARTICLE_VIEWED,
                    new ArticleViewedEventPayload(articleId, 10L, timeProvider.now())
                )
            );

    }

}