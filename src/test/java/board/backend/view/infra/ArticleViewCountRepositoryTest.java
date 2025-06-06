package board.backend.view.infra;

import board.backend.common.infra.TestRepository;
import board.backend.view.domain.ArticleViewCount;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(CustomArticleViewCountRepositoryImpl.class)
class ArticleViewCountRepositoryTest extends TestRepository {

    private final Long articleId = 1L;

    @Autowired
    private EntityManager em;

    @Autowired
    private ArticleViewCountRepository articleViewCountRepository;

    @BeforeEach
    void setUp() {
        ArticleViewCount viewC = ArticleViewCount.init(articleId);
        articleViewCountRepository.save(viewC);
    }

    @Test
    @DisplayName("조회 수를 1 증가시킨다")
    void increaseLikeCount() {
        // when
        long result = articleViewCountRepository.increase(articleId);
        em.clear();

        // then
        assertThat(result).isEqualTo(1L);
        ArticleViewCount updated = articleViewCountRepository.findById(articleId).orElseThrow();
        assertThat(updated.getViewCount()).isEqualTo(2);
    }

}