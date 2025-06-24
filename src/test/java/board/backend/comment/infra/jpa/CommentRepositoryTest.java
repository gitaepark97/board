package board.backend.comment.infra.jpa;

import board.backend.comment.application.port.CommentRepository;
import board.backend.comment.domain.Comment;
import board.backend.common.config.TestJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(CommentRepositoryImpl.class)
class CommentRepositoryTest extends TestJpaRepository {

    private final Long articleId = 1L;
    private final Long parentId = 100L;
    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("댓글이 존재하면 true를 반환한다")
    void existsById_success_whenExists_returnsTrue() {
        // given
        Comment comment = Comment.create(1L, articleId, 200L, parentId, "댓글", LocalDateTime.now());
        commentRepository.save(comment);

        // when
        boolean result = commentRepository.existsById(comment.id());

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("댓글이 존재하지 않으면 false를 반환한다")
    void existsById_success_whenNotExists_returnsFalse() {
        // when
        boolean result = commentRepository.existsById(999L);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("댓글 수가 limit 이상이면 limit만큼 반환한다")
    void countBy_success_whenOverLimit_returnsLimit() {
        // given
        for (int i = 0; i < 5; i++) {
            Comment comment = Comment.create(
                (long) i + 1,
                articleId,
                200L + i,
                parentId,
                "댓글 " + i,
                LocalDateTime.now()
            );
            commentRepository.save(comment);
        }

        // when
        int count = commentRepository.countBy(articleId, parentId, 5);

        // then
        assertThat(count).isEqualTo(5);
    }

    @Test
    @DisplayName("댓글 수가 limit 미만이면 전체 개수를 반환한다")
    void countBy_success_whenUnderLimit_returnsAll() {
        // given
        for (int i = 0; i < 10; i++) {
            Comment comment = Comment.create(
                (long) i + 1,
                articleId,
                200L + i,
                parentId,
                "댓글 " + i,
                LocalDateTime.now()
            );
            commentRepository.save(comment);
        }

        // when
        int count = commentRepository.countBy(articleId, parentId, 12);

        // then
        assertThat(count).isEqualTo(10);
    }

    @Test
    @DisplayName("조건에 맞는 댓글이 없으면 0을 반환한다")
    void countBy_success_whenNoMatch_returnsZero() {
        // when
        int count = commentRepository.countBy(999L, parentId, 5);

        // then
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("첫 페이지 댓글을 시간순으로 조회한다")
    void findAllById_success_whenFirstPage_returnsSortedComments() {
        // given
        for (int i = 0; i < 5; i++) {
            Comment comment = Comment.create(
                (long) i + 1,
                articleId,
                200L + i,
                parentId,
                "댓글 " + i,
                LocalDateTime.now()
            );
            commentRepository.save(comment);
        }

        // when
        List<Comment> comments = commentRepository.findAllById(articleId, 5L);

        // then
        assertThat(comments).hasSize(5);
        assertThat(comments.getFirst().id()).isLessThan(comments.get(4).id());
    }

    @Test
    @DisplayName("커서 이후의 다음 페이지 댓글을 조회한다")
    void findAllById_success_whenCursorGiven_returnsNextPage() {
        // given
        for (int i = 0; i < 10; i++) {
            Comment comment = Comment.create(
                (long) i + 1,
                articleId,
                200L + i,
                parentId,
                "댓글 " + i,
                LocalDateTime.now()
            );
            commentRepository.save(comment);
        }
        List<Comment> firstPage = commentRepository.findAllById(articleId, 5L);
        Comment lastComment = firstPage.get(4);

        // when
        List<Comment> nextPage = commentRepository.findAllById(
            articleId,
            5L,
            lastComment.parentId(),
            lastComment.id()
        );

        // then
        assertThat(nextPage.size()).isLessThanOrEqualTo(5);
        for (var comment : nextPage) {
            boolean isCursorAfter = comment.parentId() > lastComment.parentId()
                || (comment.parentId().equals(lastComment.parentId()) && comment.id() > lastComment.id());
            assertThat(isCursorAfter).isTrue();
        }
    }

}