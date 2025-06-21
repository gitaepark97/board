package board.backend.comment.application;

import board.backend.article.application.ArticleValidator;
import board.backend.article.application.fake.FakeArticleRepository;
import board.backend.article.domain.Article;
import board.backend.comment.application.fake.FakeArticleCommentCountRepository;
import board.backend.comment.application.fake.FakeArticleCommentCountSnapshotRepository;
import board.backend.comment.application.fake.FakeCommentRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.Comment;
import board.backend.comment.domain.CommentNotFound;
import board.backend.common.event.EventType;
import board.backend.common.event.fake.FakeEventPublisher;
import board.backend.common.event.payload.CommentCreatedEventPayload;
import board.backend.common.infra.fake.FakeCachedRepository;
import board.backend.common.support.fake.FakeIdProvider;
import board.backend.common.support.fake.FakeTimeProvider;
import board.backend.user.application.UserValidator;
import board.backend.user.application.fake.FakeUserRepository;
import board.backend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommentCreatorTest {

    private final Long id = 1L;
    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    FakeTimeProvider timeProvider;
    private FakeCachedRepository<ArticleCommentCount, Long> cachedRepository;
    private FakeCommentRepository commentRepository;
    private FakeArticleCommentCountRepository commentCountRepository;
    private FakeEventPublisher eventPublisher;
    private FakeUserRepository userRepository;
    private FakeArticleRepository articleRepository;
    private CommentCreator commentCreator;

    @BeforeEach
    void setUp() {
        timeProvider = new FakeTimeProvider(now);
        cachedRepository = new FakeCachedRepository<>();
        commentRepository = new FakeCommentRepository();
        commentCountRepository = new FakeArticleCommentCountRepository();
        eventPublisher = new FakeEventPublisher();
        userRepository = new FakeUserRepository();
        articleRepository = new FakeArticleRepository();
        commentCreator = new CommentCreator(
            new FakeIdProvider(id),
            timeProvider,
            cachedRepository,
            commentRepository,
            commentCountRepository,
            eventPublisher,
            new UserValidator(new FakeCachedRepository<>(), userRepository),
            new ArticleValidator(new FakeCachedRepository<>(), articleRepository),
            new TodayCommentCountCalculator(timeProvider, commentCountRepository, new FakeArticleCommentCountSnapshotRepository())
        );
    }

    @Test
    @DisplayName("부모 댓글이 없을 때 댓글 생성에 성공한다")
    void create_success_whenNoParentComment_returnsNewComment() {
        // given
        Long articleId = 1L;
        Long userId = 100L;
        String content = "댓글 내용";
        articleRepository.save(Article.create(articleId, 10L, userId, "제목", "내용", LocalDateTime.now()));
        userRepository.save(User.create(userId, "user@example.com", "닉네임", LocalDateTime.now()));

        // when
        Comment comment = commentCreator.create(articleId, userId, null, content);

        // then
        assertThat(comment.id()).isEqualTo(id);
        assertThat(comment.articleId()).isEqualTo(articleId);
        assertThat(comment.writerId()).isEqualTo(userId);
        assertThat(comment.content()).isEqualTo(content);
        assertThat(comment.createdAt()).isEqualTo(now);
        assertThat(commentRepository.findById(comment.id())).contains(comment);

        // 댓글 수 증가했는지 확인
        assertThat(commentCountRepository.findById(articleId)).contains(new ArticleCommentCount(articleId, 1L));
        assertThat(cachedRepository.findByKey(articleId)).isEmpty();

        // 이벤트 발행 확인
        assertThat(eventPublisher.getPublishedEvents())
            .containsExactly(
                new FakeEventPublisher.PublishedEvent(
                    EventType.COMMENT_CREATED,
                    new CommentCreatedEventPayload(articleId, 1L, timeProvider.now())
                )
            );
    }

    @Test
    @DisplayName("부모 댓글이 존재할 때 댓글 생성에 성공한다")
    void create_success_whenParentCommentExists_returnsNewComment() {
        // given
        User user = User.create(100L, "user@example.com", "닉네임", LocalDateTime.now());
        Article article = Article.create(1L, 10L, user.id(), "제목", "내용", LocalDateTime.now());
        Comment parentComment = Comment.create(500L, article.id(), user.id(), null, "부모댓글", LocalDateTime.now());
        userRepository.save(user);
        articleRepository.save(article);
        commentRepository.save(parentComment);

        String content = "대댓글";

        // when
        Comment comment = commentCreator.create(article.id(), user.id(), parentComment.id(), content);

        // then
        assertThat(comment.parentId()).isEqualTo(parentComment.id());
    }

    @Test
    @DisplayName("부모 댓글이 존재하지 않으면 예외가 발생한다")
    void create_fail_whenParentCommentNotExists_throwsException() {
        // given
        Long articleId = 1L;
        Long userId = 100L;
        Long parentId = 999L;

        articleRepository.save(Article.create(articleId, 10L, userId, "제목", "내용", LocalDateTime.now()));
        userRepository.save(User.create(userId, "user@example.com", "닉네임", LocalDateTime.now()));

        // when & then
        assertThatThrownBy(() -> commentCreator.create(articleId, userId, parentId, "댓글"))
            .isInstanceOf(CommentNotFound.class);
    }

}