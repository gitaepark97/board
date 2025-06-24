package board.backend.common.count.application;

import board.backend.common.count.application.fake.FakeArticleCountRepository;
import board.backend.common.count.application.fake.FakeArticleCountSnapshotRepository;
import board.backend.common.support.fake.FakeTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AbstractArticleCountSnapshotCreatorTest {

    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    private FakeTimeProvider timeProvider;
    private FakeArticleCountRepository<DummyArticleCount> articleCountRepository;
    private FakeArticleCountSnapshotRepository<DummyArticleCountSnapshot> snapshotRepository;
    private DummyArticleCountSnapshotCreator snapshotCreator;

    @BeforeEach
    void setUp() {
        timeProvider = new FakeTimeProvider(now);
        articleCountRepository = new FakeArticleCountRepository<>();
        snapshotRepository = new FakeArticleCountSnapshotRepository<>();
        snapshotCreator = new DummyArticleCountSnapshotCreator(
            timeProvider,
            articleCountRepository,
            snapshotRepository
        );
    }

    @Test
    @DisplayName("ArticleCount가 존재하면 스냅샷을 생성하고 저장한다")
    void createCountSnapshot_success_whenArticleCountsExist_savesSnapshots() {
        // given
        DummyArticleCount count1 = DummyArticleCount.create(1L, 5L);
        DummyArticleCount count2 = DummyArticleCount.create(2L, 10L);
        articleCountRepository.save(count1);
        articleCountRepository.save(count2);

        // when
        snapshotCreator.createCountSnapshot();

        // then
        LocalDate yesterday = timeProvider.yesterday();
        Optional<DummyArticleCountSnapshot> snapshot1 =
            snapshotRepository.findByDateAndArticleId(yesterday, count1.getArticleId());
        Optional<DummyArticleCountSnapshot> snapshot2 =
            snapshotRepository.findByDateAndArticleId(yesterday, count2.getArticleId());

        assertThat(snapshot1).isPresent();
        assertThat(snapshot1.get().getCount()).isEqualTo(count1.getCount());
        assertThat(snapshot2).isPresent();
        assertThat(snapshot2.get().getCount()).isEqualTo(count2.getCount());
    }

    @Test
    @DisplayName("count가 0이면 스냅샷을 저장하지 않는다")
    void createCountSnapshot_success_whenCountIsZero_doesNotSaveSnapshot() {
        // given
        DummyArticleCount zeroCount = DummyArticleCount.create(3L, 0L);
        articleCountRepository.save(zeroCount);

        // when
        snapshotCreator.createCountSnapshot();

        // then
        LocalDate yesterday = timeProvider.yesterday();
        Optional<DummyArticleCountSnapshot> snapshot =
            snapshotRepository.findByDateAndArticleId(yesterday, 3L);

        assertThat(snapshot).isEmpty();
    }

}