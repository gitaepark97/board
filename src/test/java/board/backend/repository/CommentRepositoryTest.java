package board.backend.repository;

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
        // given
        commentRepository.deleteAll();
        for (int i = 0; i < 3; i++) {
            Comment comment = Comment.create(
                (long) i + 100,
                articleId,
                300L + i,
                parentId,
                "적은 댓글 " + i,
                LocalDateTime.now()
            );
            commentRepository.save(comment);
        }

        // when
        int count = commentRepository.countBy(articleId, parentId, 5);

        // then
        assertThat(count).isEqualTo(3);
    }

    @Test
    @DisplayName("조건에 맞는 댓글이 없으면 0을 반환한다")
    void countBy_noMatch() {
        // when
        int count = commentRepository.countBy(999L, parentId, 5);

        // then
        assertThat(count).isEqualTo(0);
    }

}