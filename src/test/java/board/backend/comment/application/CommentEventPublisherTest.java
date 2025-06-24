package board.backend.comment.application;

import board.backend.comment.application.fake.FakeArticleCommentCountRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.ArticleCommentCountSnapshot;
import board.backend.comment.domain.Comment;
import board.backend.common.count.application.fake.FakeArticleCountSnapshotRepository;
import board.backend.common.event.EventType;
import board.backend.common.event.fake.FakeEventPublisher;
import board.backend.common.event.payload.ArticleCommentCountChangedEventPayload;
import board.backend.common.support.fake.FakeTimeProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class CommentEventPublisherTest {

    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    private FakeTimeProvider timeProvider;
    private FakeArticleCommentCountRepository fakeArticleCommentCountRepository;
    private FakeEventPublisher eventPublisher;
    private CommentEventPublisher commentEventPublisher;

    @BeforeEach
    void setUp() {
        timeProvider = new FakeTimeProvider(now);
        fakeArticleCommentCountRepository = new FakeArticleCommentCountRepository();
        eventPublisher = new FakeEventPublisher();
        FakeArticleCountSnapshotRepository<ArticleCommentCountSnapshot> articleCountSnapshotRepository = new FakeArticleCountSnapshotRepository<>();
        TodayCommentCountCalculator todayCommentCountCalculator = new TodayCommentCountCalculator(timeProvider, articleCountSnapshotRepository);
        commentEventPublisher = new CommentEventPublisher(
            timeProvider,
            fakeArticleCommentCountRepository,
            eventPublisher,
            todayCommentCountCalculator
        );
    }

    @Test
    @DisplayName("ArticleCommentCount가 존재하면 이벤트를 발행한다")
    void publishEvent_success_publishesEvent() {
        // given
        Long articleId = 1L;
        Comment comment = Comment.create(1L, articleId, 1L, null, "댓글1", LocalDateTime.now());
        fakeArticleCommentCountRepository.save(ArticleCommentCount.init(articleId));

        // when
        commentEventPublisher.publishEvent(comment);

        // then
        Assertions.assertThat(eventPublisher.getPublishedEvents())
            .containsExactly(
                new FakeEventPublisher.PublishedEvent(
                    EventType.ARTICLE_COMMENT_COUNT_CHANGED,
                    new ArticleCommentCountChangedEventPayload(articleId, 1L, timeProvider.now())
                )
            );
    }

}