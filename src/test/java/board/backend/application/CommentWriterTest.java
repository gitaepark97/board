package board.backend.application;

import board.backend.domain.Comment;
import board.backend.repository.CommentRepository;
import board.backend.support.IdProvider;
import board.backend.support.TimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommentWriterTest {

    private IdProvider idProvider;
    private TimeProvider timeProvider;
    private CommentRepository commentRepository;
    private CommentWriter commentWriter;

    @BeforeEach
    void setUp() {
        idProvider = mock(IdProvider.class);
        timeProvider = mock(TimeProvider.class);
        commentRepository = mock(CommentRepository.class);
        commentWriter = new CommentWriter(idProvider, timeProvider, commentRepository);
    }

    @Test
    @DisplayName("댓글 생성에 성공한다")
    void createComment_success() {
        // given
        Long commentId = 1L;
        Long articleId = 10L;
        Long writerId = 100L;
        Long parentCommentId = 1L; // 자기 자신을 부모로 둘 수 있음 (isRoot 조건)
        String content = "댓글 내용입니다.";
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        when(idProvider.nextId()).thenReturn(commentId);
        when(timeProvider.now()).thenReturn(now);

        // when
        Comment comment = commentWriter.create(articleId, writerId, parentCommentId, content);

        // then
        assertThat(comment.getId()).isEqualTo(commentId);
        assertThat(comment.getArticleId()).isEqualTo(articleId);
        assertThat(comment.getWriterId()).isEqualTo(writerId);
        assertThat(comment.getParentId()).isEqualTo(parentCommentId);
        assertThat(comment.getContent()).isEqualTo(content);
        assertThat(comment.getCreatedAt()).isEqualTo(now);
    }

}