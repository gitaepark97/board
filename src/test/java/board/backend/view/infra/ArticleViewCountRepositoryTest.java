package board.backend.view.infra;

import board.backend.common.infra.TestRepository;
import board.backend.view.domain.ArticleViewCount;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ArticleViewCountRepositoryTest extends TestRepository {

    private final Long articleId = 1L;

    @Autowired
    private EntityManager em;

    @Autowired
    private ArticleViewCountRepository articleViewCountRepository;

    @BeforeEach
    void setUp() {
        ArticleViewCount viewCount = ArticleViewCount.init(articleId);
        articleViewCountRepository.save(viewCount);
    }

    @Test
    @DisplayName("존재하지 않으면 insert 된다")
    void upsert_insert() {
        // given
        Long newArticleId = 2L;

        // when
        articleViewCountRepository.upsert(newArticleId, 1L);
        em.flush();
        em.clear();

        // then
        var saved = articleViewCountRepository.findById(newArticleId).orElseThrow();
        assertThat(saved.getViewCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("이미 존재하면 view_count가 1 증가한다")
    void upsert_update() {
        // when
        articleViewCountRepository.upsert(articleId, 1L);
        em.flush();
        em.clear();

        // then
        var updated = articleViewCountRepository.findById(articleId).orElseThrow();
        assertThat(updated.getViewCount()).isEqualTo(2L);
    }

}