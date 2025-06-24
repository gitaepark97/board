package board.backend.comment.application;

import board.backend.comment.application.fake.FakeArticleCommentCountRepository;
import board.backend.comment.application.fake.FakeCommentRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.Comment;
import board.backend.common.cache.fake.FakeCachedRepository;
import board.backend.common.count.application.fake.FakeArticleCountSnapshotRepository;
import board.backend.common.event.EventType;
import board.backend.common.event.fake.FakeEventPublisher;
import board.backend.common.event.payload.ArticleCommentCountChangedEventPayload;
import board.backend.common.support.fake.FakeTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CommentDeleterTest {

    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    private FakeCachedRepository<ArticleCommentCount, Long> cachedRepository;
    private FakeCommentRepository commentRepository;
    private FakeArticleCommentCountRepository commentCountRepository;
    private FakeEventPublisher eventPublisher;
    private CommentDeleter commentDeleter;


    @BeforeEach
    void setUp() {
        FakeTimeProvider timeProvider = new FakeTimeProvider(now);
        cachedRepository = new FakeCachedRepository<>();
        commentRepository = new FakeCommentRepository();
        commentCountRepository = new FakeArticleCommentCountRepository();
        eventPublisher = new FakeEventPublisher();
        commentDeleter = new CommentDeleter(
            timeProvider,
            cachedRepository,
            commentRepository,
            commentCountRepository,
            new CommentEventPublisher(
                timeProvider,
                commentCountRepository,
                eventPublisher,
                new TodayCommentCountCalculator(
                    timeProvider,
                    new FakeArticleCountSnapshotRepository<>()
                )
            )
        );
    }

    @Test
    @DisplayName("자식이 없는 오늘 생성된 루트 댓글을 삭제하면 실제로 삭제시키고 이벤트를 발생시킨다")
    void delete_success_whenNoChildrenAndCreatedTodayComment_removesCommentAndAndPublishesEvent() {
        // given
        Comment comment = Comment.create(1L, 100L, 10L, null, "내용", now);
        commentRepository.save(comment);
        commentCountRepository.save(ArticleCommentCount.builder().articleId(comment.articleId()).count(1L).build());

        // when
        Optional<Long> result = commentDeleter.delete(comment.id(), comment.writerId());

        // then
        assertThat(result).contains(100L);
        assertThat(commentRepository.findById(comment.id())).isEmpty();
        assertThat(eventPublisher.getPublishedEvents())
            .containsExactly(
                new FakeEventPublisher.PublishedEvent(
                    EventType.ARTICLE_COMMENT_COUNT_CHANGED,
                    new ArticleCommentCountChangedEventPayload(comment.articleId(), 0L, now)
                )
            );
    }

    @Test
    @DisplayName("자식이 없는 오늘 생성되지 않은 루트 댓글을 삭제하면 실제로 삭제된다")
    void delete_success_whenNoChildrenAndNotCreatedToday_removesComment() {
        // given
        Comment comment = Comment.create(1L, 100L, 10L, null, "내용", LocalDateTime.now());
        commentRepository.save(comment);

        // when
        Optional<Long> result = commentDeleter.delete(comment.id(), comment.writerId());

        // then
        assertThat(result).contains(100L);
        assertThat(commentRepository.findById(comment.id())).isEmpty();
        assertThat(eventPublisher.getPublishedEvents()).isEmpty();
    }

    @Test
    @DisplayName("자식이 있는 댓글은 isDeleted 처리된다")
    void delete_softDelete_whenHasChildren() {
        // given
        Comment parent = Comment.create(1L, 100L, 10L, null, "부모", now);
        Comment child = Comment.create(2L, 100L, 10L, 1L, "자식", now);
        commentRepository.save(parent);
        commentRepository.save(child);

        // 자식이 있다고 판단되게 설정
        commentRepository.setCountBy(100L, 1L, 2, 2);

        // when
        commentDeleter.delete(parent.id(), parent.writerId());

        // then
        assertThat(commentRepository.findById(parent.id()))
            .map(Comment::isDeleted).contains(true);
    }

    @Test
    @DisplayName("자식이 없고 부모도 isDeleted이면 부모까지 삭제된다")
    void delete_alsoDeletesParent_whenParentIsDeletedAndNoOtherChildren() {
        // given
        Comment parent = Comment.create(1L, 100L, 10L, null, "부모", now).delete();
        Comment child = Comment.create(2L, parent.articleId(), 10L, 1L, "자식", now);
        commentRepository.save(parent);
        commentRepository.save(child);
        commentCountRepository.save(ArticleCommentCount.builder().articleId(parent.articleId()).count(2L).build());

        // 자식이 없다고 판단되게 설정
        commentRepository.setCountBy(parent.articleId(), child.id(), 2, 0);

        // when
        commentDeleter.delete(child.id(), 10L);

        // then
        assertThat(commentRepository.findById(parent.id())).isEmpty();
        assertThat(commentRepository.findById(child.id())).isEmpty();
    }

    @Test
    @DisplayName("게시글 댓글 삭제 시 댓글과 카운트, 캐시를 삭제한다")
    void deleteArticle_success_removesAllRelatedData() {
        // given
        Comment comment = Comment.create(1L, 200L, 10L, null, "댓글", now);
        ArticleCommentCount count = ArticleCommentCount.builder().articleId(comment.articleId()).count(1L).build();
        commentRepository.save(comment);
        commentCountRepository.save(count);
        cachedRepository.save(count.getArticleId(), count, Duration.ofMinutes(10));

        // when
        commentDeleter.deleteArticle(comment.articleId());

        // then
        assertThat(commentRepository.findById(comment.id())).isEmpty();
        assertThat(commentCountRepository.findById(count.getArticleId())).isEmpty();
        assertThat(cachedRepository.findByKey(count.getCount())).isEmpty();
    }

}