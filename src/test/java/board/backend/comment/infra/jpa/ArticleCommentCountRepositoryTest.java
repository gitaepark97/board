package board.backend.comment.infra.jpa;

import board.backend.comment.application.port.ArticleCommentCountRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.common.config.TestJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ArticleCommentCountRepositoryImpl.class)
class ArticleCommentCountRepositoryTest extends TestJpaRepository {

    @Autowired
    private ArticleCommentCountRepository articleCommentCountRepository;
    @Autowired
    private ArticleCommentCountEntityRepository articleCommentCountEntityRepository;

    @Test
    @DisplayName("댓글 수가 존재하지 않으면 1로 저장된다")
    void increase_success_whenNotExists_insertsWithOne() {
        // given
        Long newArticleId = 1L;

        // when
        articleCommentCountRepository.increase(newArticleId);
        em.flush();
        em.clear();

        // then
        ArticleCommentCount saved = articleCommentCountRepository.findById(newArticleId).orElseThrow();
        assertThat(saved.getCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("댓글 수가 존재하면 1 증가한다")
    void increase_success_whenExists_incrementsCount() {
        // given
        ArticleCommentCount count = ArticleCommentCount.init(1L);
        articleCommentCountEntityRepository.save(ArticleCommentCountEntity.from(count));

        // when
        articleCommentCountRepository.increase(count.getArticleId());
        em.flush();
        em.clear();

        // then
        ArticleCommentCount updated = articleCommentCountRepository.findById(count.getArticleId()).orElseThrow();
        assertThat(updated.getCount()).isEqualTo(2L);
    }

    @Test
    @DisplayName("댓글 수를 1 감소시킨다")
    void decrease_success_decrementsCount() {
        // given
        ArticleCommentCount count = ArticleCommentCount.init(1L);
        articleCommentCountEntityRepository.save(ArticleCommentCountEntity.from(count));

        // given
        articleCommentCountRepository.decrease(count.getArticleId());
        em.flush();
        em.clear();

        // when
        ArticleCommentCount updated = articleCommentCountRepository.findById(count.getArticleId()).orElseThrow();
        assertThat(updated.getCount()).isEqualTo(0L);
    }

}
