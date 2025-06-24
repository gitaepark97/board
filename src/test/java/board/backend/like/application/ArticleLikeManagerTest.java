package board.backend.like.application;

import board.backend.article.application.ArticleValidator;
import board.backend.article.application.fake.FakeArticleRepository;
import board.backend.article.domain.Article;
import board.backend.common.cache.fake.FakeCachedRepository;
import board.backend.common.count.application.fake.FakeArticleCountSnapshotRepository;
import board.backend.common.event.EventType;
import board.backend.common.event.fake.FakeEventPublisher;
import board.backend.common.event.payload.ArticleLikeCountChangedEventPayload;
import board.backend.common.support.fake.FakeTimeProvider;
import board.backend.like.application.fake.FakeArticleLikeCountRepository;
import board.backend.like.application.fake.FakeArticleLikeRepository;
import board.backend.like.domain.ArticleLike;
import board.backend.like.domain.ArticleLikeCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleLikeManagerTest {

    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    private FakeTimeProvider timeProvider;
    private FakeArticleLikeRepository articleLikeRepository;
    private FakeArticleLikeCountRepository articleLikeCountRepository;
    private FakeEventPublisher eventPublisher;
    private ArticleLikeManager articleLikeManager;

    @BeforeEach
    void setUp() {
        timeProvider = new FakeTimeProvider(now);
        articleLikeRepository = new FakeArticleLikeRepository();
        articleLikeCountRepository = new FakeArticleLikeCountRepository();
        eventPublisher = new FakeEventPublisher();
        FakeArticleRepository articleRepository = new FakeArticleRepository();
        articleLikeManager = new ArticleLikeManager(
            timeProvider,
            articleLikeRepository,
            articleLikeCountRepository,
            eventPublisher,
            new ArticleValidator(new FakeCachedRepository<>(), articleRepository),
            new TodayLikeCountCalculatorImpl(timeProvider, new FakeArticleCountSnapshotRepository<>())
        );

        articleRepository.save(Article.create(1L, 1L, 1L, "제목", "내용", timeProvider.now()));
    }

    @Test
    @DisplayName("좋아요가 없으면 좋아요를 저장하고 수를 증가시키며 이벤트를 발행한다")
    void like_success_whenNotExists_savesLikeIncrementsAndPublishesEvent() {
        // given
        Long articleId = 1L;
        Long userId = 10L;

        // when
        articleLikeManager.like(articleId, userId);

        // then
        assertThat(articleLikeRepository.existsByArticleIdAndUserId(articleId, userId)).isTrue();
        assertThat(articleLikeCountRepository.findById(articleId)).isPresent()
            .get().extracting(ArticleLikeCount::getCount).isEqualTo(1L);
        assertThat(eventPublisher.getPublishedEvents())
            .containsExactly(
                new FakeEventPublisher.PublishedEvent(
                    EventType.ARTICLE_LIKE_COUNT_CHANGED,
                    new ArticleLikeCountChangedEventPayload(articleId, 1L, timeProvider.now())
                )
            );
    }

    @Test
    @DisplayName("이미 좋아요가 존재하면 아무 작업도 하지 않는다")
    void like_noop_whenAlreadyExists_doesNothing() {
        // given
        ArticleLike articleLike = ArticleLike.create(1L, 10L, LocalDateTime.now());
        articleLikeRepository.save(articleLike);

        // when
        articleLikeManager.like(articleLike.articleId(), articleLike.userId());

        // then
        assertThat(articleLikeCountRepository.findById(articleLike.articleId())).isEmpty();
        assertThat(eventPublisher.getPublishedEvents()).isEmpty();
    }

    @Test
    @DisplayName("오늘 생성된 좋아요가 존재하면 삭제하고 수를 감소시키며 이벤트를 발행한다")
    void unlike_success_whenExistsAndCreatedToday_removesLikeDecrementsAndPublishesEvent() {
        // given
        Long articleId = 1L;
        ArticleLike articleLike = ArticleLike.create(articleId, 10L, now);
        ArticleLikeCount count = ArticleLikeCount.init(articleId);
        articleLikeRepository.save(articleLike);
        articleLikeCountRepository.increase(articleId);

        // when
        articleLikeManager.unlike(articleId, articleLike.userId());

        // then
        assertThat(articleLikeRepository.existsByArticleIdAndUserId(articleId, articleLike.userId())).isFalse();
        assertThat(articleLikeCountRepository.findById(articleId)).isPresent()
            .get().extracting(ArticleLikeCount::getCount).isEqualTo(0L);
        assertThat(eventPublisher.getPublishedEvents())
            .containsExactly(
                new FakeEventPublisher.PublishedEvent(
                    EventType.ARTICLE_LIKE_COUNT_CHANGED,
                    new ArticleLikeCountChangedEventPayload(articleId, 0L, timeProvider.now())
                )
            );
    }

    @Test
    @DisplayName("오늘 생성되지 않은 좋아요가 존재하면 삭제하고 수를 감소시킨다")
    void unlike_success_whenExistsAndNotCreatedToday_removesLikeDecrements() {
        // given
        Long articleId = 1L;
        ArticleLike articleLike = ArticleLike.create(articleId, 10L, LocalDateTime.now());
        ArticleLikeCount count = ArticleLikeCount.init(articleId);
        articleLikeRepository.save(articleLike);
        articleLikeCountRepository.increase(articleId);

        // when
        articleLikeManager.unlike(articleId, articleLike.userId());

        // then
        assertThat(articleLikeRepository.existsByArticleIdAndUserId(articleId, articleLike.userId())).isFalse();
        assertThat(articleLikeCountRepository.findById(articleId)).isPresent()
            .get().extracting(ArticleLikeCount::getCount).isEqualTo(0L);
        assertThat(eventPublisher.getPublishedEvents()).isEmpty();
    }

    @Test
    @DisplayName("좋아요가 없으면 아무 작업도 하지 않는다")
    void unlike_noop_whenNotExists_doesNothing() {
        // given
        Long articleId = 1L;
        Long userId = 10L;

        // when
        articleLikeManager.unlike(articleId, userId);

        // then
        assertThat(eventPublisher.getPublishedEvents()).isEmpty();
    }

}