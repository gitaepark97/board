package board.backend.infra;

import board.backend.TestcontainersConfiguration;
import board.backend.domain.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@Import({
    TestcontainersConfiguration.class,
    QueryDSLConfig.class,
    CustomCommentRepositoryImpl.class
})
@DataJpaTest
class CommentRepositoryTest {

    private final Long articleId = 1L;
    private final Long parentId = 100L;
    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    @DisplayName("댓글 수를 limit만큼 조회하고 그 개수를 반환한다")
    void countBy_withLimit() {
        // when
        int count = commentRepository.countBy(articleId, parentId, 5);

        // then
        assertThat(count).isEqualTo(5);
    }

    @Test
    @DisplayName("댓글 수가 limit보다 적을 경우 전체 개수를 반환한다")
    void countBy_underLimit() {
        // when
        int count = commentRepository.countBy(articleId, parentId, 12);

        // then
        assertThat(count).isEqualTo(10);
    }

    @Test
    @DisplayName("조건에 맞는 댓글이 없으면 0을 반환한다")
    void countBy_noMatch() {
        // when
        int count = commentRepository.countBy(999L, parentId, 5);

        // then
        assertThat(count).isEqualTo(0);
    }

    @Test
    @DisplayName("시간순 정렬로 댓글을 pageSize만큼 조회한다 (첫 페이지)")
    void findAllByArticleId_firstPage() {
        // when
        var comments = commentRepository.findAllById(articleId, 5L);

        // then
        assertThat(comments.size()).isEqualTo(5);
        assertThat(comments.getFirst().getId()).isLessThan(comments.get(4).getId());
    }

    @Test
    @DisplayName("커서 기반으로 다음 페이지의 댓글을 조회한다")
    void findAllByArticleId_nextPage() {
        // given
        var firstPage = commentRepository.findAllById(articleId, 5L);
        var lastComment = firstPage.get(4);

        // when
        var nextPage = commentRepository.findAllById(
            articleId,
            5L,
            lastComment.getParentId(),
            lastComment.getId()
        );

        // then
        assertThat(nextPage.size()).isLessThanOrEqualTo(5);
        for (var comment : nextPage) {
            boolean isCursorAfter = comment.getParentId() > lastComment.getParentId()
                || (comment.getParentId().equals(lastComment.getParentId()) && comment.getId() > lastComment.getId());
            assertThat(isCursorAfter).isTrue();
        }
    }

}