package board.backend.article.application;

import board.backend.article.application.fake.FakeArticleRepository;
import board.backend.article.domain.Article;
import board.backend.board.application.BoardValidator;
import board.backend.board.application.fake.FakeBoardRepository;
import board.backend.board.domain.Board;
import board.backend.common.cache.fake.FakeCachedRepository;
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

class ArticleCreatorTest {

    private final Long id = 1L;
    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    private FakeArticleRepository articleRepository;
    private FakeUserRepository userRepository;
    private FakeBoardRepository boardRepository;
    private ArticleCreator articleCreator;

    @BeforeEach
    void setUp() {
        articleRepository = new FakeArticleRepository();
        userRepository = new FakeUserRepository();
        boardRepository = new FakeBoardRepository();
        UserValidator userValidator = new UserValidator(new FakeCachedRepository<>(), userRepository);
        BoardValidator boardValidator = new BoardValidator(boardRepository);
        articleCreator = new ArticleCreator(
            new FakeIdProvider(id),
            new FakeTimeProvider(now),
            articleRepository,
            userValidator,
            boardValidator
        );
    }

    @Test
    @DisplayName("게시글을 생성할 수 있다")
    void create_success_whenBoardAndUserExist_createsArticleAndPublishesEvent() {
        // given
        String title = "제목";
        String content = "내용";
        Board board = new Board(1L, "공지사항", LocalDateTime.now());
        User user = User.create(10L, "user@example.com", "nickname", LocalDateTime.now());
        boardRepository.save(board);
        userRepository.save(user);

        // when
        Article article = articleCreator.create(board.id(), user.id(), title, content);

        // then
        assertThat(article.id()).isEqualTo(id);
        assertThat(article.boardId()).isEqualTo(board.id());
        assertThat(article.writerId()).isEqualTo(user.id());
        assertThat(article.title()).isEqualTo(title);
        assertThat(article.content()).isEqualTo(content);
        assertThat(article.createdAt()).isEqualTo(now);

        assertThat(articleRepository.findById(article.id())).contains(article);
    }

}