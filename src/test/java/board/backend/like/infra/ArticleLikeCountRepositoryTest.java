package board.backend.like.infra;

import board.backend.common.infra.TestRepository;
import board.backend.like.domain.ArticleLikeCount;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(CustomArticleLikeCountRepositoryImpl.class)
class ArticleLikeCountRepositoryTest extends TestRepository {

    private final Long articleId = 1L;

    @Autowired
    private EntityManager em;

    @Autowired
    private ArticleLikeCountRepository articleLikeCountRepository;

    @BeforeEach
    void setUp() {
        ArticleLikeCount likeCount = ArticleLikeCount.init(articleId);
        articleLikeCountRepository.save(likeCount);
    }

    @Test
    @DisplayName("좋아요 수를 1 증가시킨다")
    void increaseLikeCount() {
        // when
        long result = articleLikeCountRepository.increase(articleId);
        em.clear();

        // then
        assertThat(result).isEqualTo(1L);
        ArticleLikeCount updated = articleLikeCountRepository.findById(articleId).orElseThrow();
        assertThat(updated.getLikeCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("좋아요 수를 1 감소시킨다")
    void decreaseLikeCount() {
        // when
        articleLikeCountRepository.decrease(articleId);
        em.clear();

        // then
        ArticleLikeCount updated = articleLikeCountRepository.findById(articleId).orElseThrow();
        assertThat(updated.getLikeCount()).isEqualTo(0);
    }

}