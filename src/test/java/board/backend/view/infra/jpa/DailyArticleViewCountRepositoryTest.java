package board.backend.view.infra.jpa;

import board.backend.common.infra.TestRepository;
import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(ArticleViewCountRepositoryImpl.class)
class DailyArticleViewCountRepositoryTest extends TestRepository {

    private final Long articleId = 1L;

    @Autowired
    private ArticleViewCountRepository articleViewCountRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        ArticleViewCount viewCount = ArticleViewCount.init(articleId);
        articleViewCountRepository.save(viewCount);
    }

    @Test
    @DisplayName("조회 수를 1 증가시킨다")
    void increaseLikeCount() {
        // when
        articleViewCountRepository.increase(articleId);
        em.clear();

        // then
        var updated = articleViewCountRepository.findById(articleId).orElseThrow();
        assertThat(updated.viewCount()).isEqualTo(2);
    }

}