package board.backend.like.application;

import board.backend.common.support.fake.FakeTimeProvider;
import board.backend.like.application.fake.FakeArticleCommentRepository;
import board.backend.like.application.fake.FakeArticleLikeCountSnapshotRepository;
import board.backend.like.domain.ArticleLikeCount;
import board.backend.like.domain.ArticleLikeCountSnapshot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LikeCountSnapshotCreatorTest {

    private FakeTimeProvider timeProvider;
    private FakeArticleCommentRepository articleLikeCountRepository;
    private FakeArticleLikeCountSnapshotRepository articleViewCountSnapshotRepository;
    private LikeCountSnapshotCreator likeCountSnapshotCreator;

    @BeforeEach
    void setUp() {
        timeProvider = new FakeTimeProvider(LocalDateTime.of(2024, 1, 1, 12, 0));
        articleLikeCountRepository = new FakeArticleCommentRepository();
        articleViewCountSnapshotRepository = new FakeArticleLikeCountSnapshotRepository();
        likeCountSnapshotCreator = new LikeCountSnapshotCreator(
            timeProvider,
            articleLikeCountRepository,
            articleViewCountSnapshotRepository
        );
    }

    @Test
    @DisplayName("0 이상의 조회수만 스냅샷으로 저장된다")
    void createCountSnapshot_onlyPositiveViewCountGetsSaved() {
        // given
        ArticleLikeCount count1 = new ArticleLikeCount(1L, 0L);
        ArticleLikeCount count2 = new ArticleLikeCount(2L, 100L);
        articleLikeCountRepository.save(count1);
        articleLikeCountRepository.save(count2);

        // when
        likeCountSnapshotCreator.createCountSnapshot();

        // then
        var snapshot1 = articleViewCountSnapshotRepository.findByDateAndArticleId(timeProvider.yesterday(), count1.articleId());
        assertThat(snapshot1).isEmpty();
        var snapshot2 = articleViewCountSnapshotRepository.findByDateAndArticleId(timeProvider.yesterday(), count2.articleId());
        assertThat(snapshot2).isPresent();
        assertThat(snapshot2.get())
            .extracting(ArticleLikeCountSnapshot::articleId, ArticleLikeCountSnapshot::likeCount)
            .containsExactly(count2.articleId(), count2.likeCount());
    }

}