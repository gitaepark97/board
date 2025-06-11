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
    @DisplayName("존재하지 않으면 insert 된다")
    void increaseOrSave_insert() {
        // given
        Long newArticleId = 2L;

        // when
        articleLikeCountRepository.increaseOrSave(newArticleId, 1L);
        em.flush();
        em.clear();

        // then
        var saved = articleLikeCountRepository.findById(newArticleId).orElseThrow();
        assertThat(saved.getLikeCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("이미 존재하면 comment_count가 1 증가한다")
    void increaseOrSave_update() {
        // when
        articleLikeCountRepository.increaseOrSave(articleId, 1L);
        em.flush();
        em.clear();

        // then
        var updated = articleLikeCountRepository.findById(articleId).orElseThrow();
        assertThat(updated.getLikeCount()).isEqualTo(2L);
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