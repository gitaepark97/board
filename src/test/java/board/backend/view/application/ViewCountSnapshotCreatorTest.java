package board.backend.view.application;

import board.backend.common.support.fake.FakeTimeProvider;
import board.backend.view.application.fake.FakeArticleViewCountRepository;
import board.backend.view.application.fake.FakeArticleViewCountSnapshotRepository;
import board.backend.view.domain.ArticleViewCount;
import board.backend.view.domain.ArticleViewCountSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ViewCountSnapshotCreatorTest {

    private FakeTimeProvider timeProvider;
    private FakeArticleViewCountRepository articleViewCountRepository;
    private FakeArticleViewCountSnapshotRepository articleViewCountSnapshotRepository;
    private ViewCountSnapshotCreator viewCountSnapshotCreator;

    @BeforeEach
    void setUp() {
        timeProvider = new FakeTimeProvider(LocalDateTime.of(2024, 1, 1, 12, 0));
        articleViewCountRepository = new FakeArticleViewCountRepository();
        articleViewCountSnapshotRepository = new FakeArticleViewCountSnapshotRepository();
        viewCountSnapshotCreator = new ViewCountSnapshotCreator(
            timeProvider,
            articleViewCountRepository,
            articleViewCountSnapshotRepository
        );
    }

    @Test
    @DisplayName("0 이상의 조회수만 스냅샷으로 저장된다")
    void createCountSnapshot_onlyPositiveViewCountGetsSaved() {
        // given
        ArticleViewCount count1 = new ArticleViewCount(1L, 0L);
        ArticleViewCount count2 = new ArticleViewCount(2L, 100L);
        articleViewCountRepository.save(count1);
        articleViewCountRepository.save(count2);

        // when
        viewCountSnapshotCreator.createCountSnapshot();

        // then
        var snapshot1 = articleViewCountSnapshotRepository.findByDateAndArticleId(timeProvider.yesterday(), count1.articleId());
        assertThat(snapshot1).isEmpty();
        var snapshot2 = articleViewCountSnapshotRepository.findByDateAndArticleId(timeProvider.yesterday(), count2.articleId());
        assertThat(snapshot2).isPresent();
        assertThat(snapshot2.get())
            .extracting(ArticleViewCountSnapshot::articleId, ArticleViewCountSnapshot::viewCount)
            .containsExactly(count2.articleId(), count2.viewCount());
    }

}