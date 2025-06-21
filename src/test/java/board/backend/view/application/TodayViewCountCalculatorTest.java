package board.backend.view.application;

import board.backend.common.support.fake.FakeTimeProvider;
import board.backend.view.application.fake.FakeArticleViewCountSnapshotRepository;
import board.backend.view.domain.ArticleViewCountSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TodayViewCountCalculatorTest {

    private FakeTimeProvider timeProvider;
    private FakeArticleViewCountSnapshotRepository articleViewCountSnapshotRepository;
    private TodayViewCountCalculator todayViewCountCalculator;

    @BeforeEach
    void setUp() {
        timeProvider = new FakeTimeProvider(LocalDateTime.of(2024, 1, 1, 12, 0));
        articleViewCountSnapshotRepository = new FakeArticleViewCountSnapshotRepository();
        todayViewCountCalculator = new TodayViewCountCalculator(timeProvider, articleViewCountSnapshotRepository);
    }

    @Test
    @DisplayName("스냅샷이 존재할 때 오늘 조회수는 현재 - 어제")
    void calculate_withSnapshot() {
        // given
        articleViewCountSnapshotRepository.saveAll(List.of(
            new ArticleViewCountSnapshot(timeProvider.yesterday(), 1L, 100L)
        ));

        // when
        long result = todayViewCountCalculator.calculate(1L, 150L);

        // then
        assertThat(result).isEqualTo(50L);
    }

    @Test
    @DisplayName("스냅샷이 없을 때 오늘 조회수는 현재 값")
    void calculate_withoutSnapshot() {
        // when
        long result = todayViewCountCalculator.calculate(1L, 80L);

        // then
        assertThat(result).isEqualTo(80L);
    }

}