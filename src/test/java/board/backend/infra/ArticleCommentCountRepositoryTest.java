package board.backend.infra;

import board.backend.domain.ArticleCommentCount;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(CustomArticleCommentCountRepositoryImpl.class)
class ArticleCommentCountRepositoryTest extends TestRepository {

    private final Long articleId = 1L;
    @Autowired
    private ArticleCommentCountRepository articleCommentCountRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        ArticleCommentCount count = ArticleCommentCount.init(articleId);
        articleCommentCountRepository.save(count);
    }

    @Test
    @DisplayName("댓글 수를 1 증가시킨다")
    void increaseCommentCount() {
        // when
        articleCommentCountRepository.increase(articleId);
        em.flush();
        em.clear();

        // then
        var updated = articleCommentCountRepository.findById(articleId).orElseThrow();
        assertThat(updated.getCommentCount()).isEqualTo(2L);
    }

    @Test
    @DisplayName("댓글 수를 1 감소시킨다")
    void decreaseCommentCount() {
        // given
        articleCommentCountRepository.increase(articleId); // 1 -> 2
        em.flush();
        em.clear();

        // when
        articleCommentCountRepository.decrease(articleId);
        em.flush();
        em.clear();

        // then
        var updated = articleCommentCountRepository.findById(articleId).orElseThrow();
        assertThat(updated.getCommentCount()).isEqualTo(1L);
    }

}
