package board.backend.comment.application;

import board.backend.comment.application.dto.CommentWithWriter;
import board.backend.comment.application.fake.FakeCommentRepository;
import board.backend.comment.domain.Comment;
import board.backend.common.infra.fake.FakeCachedRepository;
import board.backend.user.application.UserReader;
import board.backend.user.application.fake.FakeUserRepository;
import board.backend.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CommentReaderTest {

    private FakeCommentRepository commentRepository;
    private FakeUserRepository userRepository;
    private CommentReader commentReader;

    @BeforeEach
    void setUp() {
        commentRepository = new FakeCommentRepository();
        userRepository = new FakeUserRepository();
        UserReader userReader = new UserReader(new FakeCachedRepository<>(), userRepository);
        commentReader = new CommentReader(commentRepository, userReader);
    }

    @Test
    @DisplayName("게시글 ID로 댓글을 조회할 수 있다 (lastId가 없는 경우)")
    void readAll_success_whenNoLastId_returnsFirstPage() {
        // given
        User user1 = User.create(10L, "user1@example.com", "user1", LocalDateTime.now());
        User user2 = User.create(11L, "user2@example.com", "user2", LocalDateTime.now());
        Comment comment1 = Comment.create(1L, 100L, user1.id(), null, "댓글1", LocalDateTime.now());
        Comment comment2 = Comment.create(2L, 100L, user2.id(), null, "댓글2", LocalDateTime.now());
        userRepository.save(user1);
        userRepository.save(user2);
        commentRepository.save(comment1);
        commentRepository.save(comment2);

        // when
        List<CommentWithWriter> result = commentReader.readAll(comment1.articleId(), 10L, null, null);

        // then
        assertThat(result).extracting(CommentWithWriter::comment)
            .containsExactlyInAnyOrder(comment1, comment2);
        assertThat(result).extracting(CommentWithWriter::writer)
            .containsExactlyInAnyOrder(user1, user2);
    }

    @Test
    @DisplayName("게시글 ID로 댓글을 조회할 수 있다 (lastId가 있는 경우)")
    void readAll_success_whenLastIdGiven_returnsNextPage() {
        // given
        Long articleId = 100L;
        User user3 = User.create(12L, "user3@example.com", "user3", LocalDateTime.now());
        User user4 = User.create(13L, "user4@example.com", "user4", LocalDateTime.now());
        Comment comment3 = Comment.create(3L, articleId, user3.id(), 1L, "대댓글1", LocalDateTime.now());
        Comment comment4 = Comment.create(4L, articleId, user4.id(), 1L, "대댓글2", LocalDateTime.now());
        userRepository.save(user3);
        userRepository.save(user4);
        commentRepository.save(comment3);
        commentRepository.save(comment4);

        // when
        List<CommentWithWriter> result = commentReader.readAll(articleId, 10L, 1L, 2L);

        // then
        assertThat(result).extracting(CommentWithWriter::comment)
            .containsExactlyInAnyOrder(comment3, comment4);
        assertThat(result).extracting(CommentWithWriter::writer)
            .containsExactlyInAnyOrder(user3, user4);
    }

}