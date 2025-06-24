package board.backend.common.count.application;

import board.backend.common.count.application.fake.FakeArticleCountRepository;
import board.backend.common.count.application.fake.FakeArticleCountSnapshotRepository;
import board.backend.common.support.fake.FakeTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractTodayCountCalculatorTest {

    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    private FakeTimeProvider timeProvider;
    private FakeArticleCountRepository<DummyArticleCount> articleCountRepository;
    private FakeArticleCountSnapshotRepository<DummyArticleCountSnapshot> articleCountSnapshotRepository;
    private DummyTodayCountCalculator todayCountCalculator;

    @BeforeEach
    void setUp() {
        timeProvider = new FakeTimeProvider(now);
        articleCountRepository = new FakeArticleCountRepository<>();
        articleCountSnapshotRepository = new FakeArticleCountSnapshotRepository<>();
        todayCountCalculator = new DummyTodayCountCalculator(timeProvider, articleCountSnapshotRepository);
    }

    @Test
    @DisplayName("어제 스냅샷이 존재하면 currentCount - yesterdayCount를 반환한다")
    void calculate_success_whenYesterdaySnapshotExists_returnsDifference() {
        // given
        DummyArticleCount articleCount = DummyArticleCount.builder().articleId(1L).count(15L).build();
        DummyArticleCountSnapshot snapshot = DummyArticleCountSnapshot.create(timeProvider.yesterday(), articleCount.getArticleId(), 10L);
        articleCountRepository.save(articleCount);
        articleCountSnapshotRepository.save(snapshot);

        // when
        long todayCount = todayCountCalculator.calculate(articleCount);

        // then
        assertThat(todayCount).isEqualTo(5L);
    }

    @Test
    @DisplayName("어제 스냅샷이 없으면 currentCount를 그대로 반환한다")
    void calculate_success_whenNoSnapshot_returnsCurrentCount() {
        // given
        DummyArticleCount articleCount = DummyArticleCount.builder().articleId(1L).count(15L).build();

        // when
        long todayCount = todayCountCalculator.calculate(articleCount);

        // then
        assertThat(todayCount).isEqualTo(articleCount.getCount());
    }

}