package board.backend.comment.infra.jpa;

import board.backend.comment.application.port.ArticleCommentCountRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.common.infra.TestRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Import(ArticleCommentCountRepositoryImpl.class)
class ArticleCommentCountRepositoryTest extends TestRepository {

    private final Long articleId = 1L;
    @Autowired
    private ArticleCommentCountRepository articleCommentCountRepository;
    @Autowired
    private ArticleCommentCountEntityRepository articleCommentCountEntityRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void setUp() {
        ArticleCommentCount count = ArticleCommentCount.init(articleId);
        articleCommentCountEntityRepository.save(ArticleCommentCountEntity.from(count));
    }

    @Test
    @DisplayName("존재하지 않으면 insert 된다")
    void increaseOrSave_insert() {
        // given
        Long newArticleId = 2L;

        // when
        articleCommentCountRepository.increaseOrSave(newArticleId, 1L);
        em.flush();
        em.clear();

        // then
        var saved = articleCommentCountRepository.findById(newArticleId).orElseThrow();
        assertThat(saved.commentCount()).isEqualTo(1L);
    }

    @Test
    @DisplayName("이미 존재하면 comment_count가 1 증가한다")
    void increaseOrSave_update() {
        // when
        articleCommentCountRepository.increaseOrSave(articleId, 1L);
        em.flush();
        em.clear();

        // then
        var updated = articleCommentCountRepository.findById(articleId).orElseThrow();
        assertThat(updated.commentCount()).isEqualTo(2L);
    }

    @Test
    @DisplayName("댓글 수를 1 감소시킨다")
    void decreaseCommentCount() {
        // when
        articleCommentCountRepository.decrease(articleId);
        em.flush();
        em.clear();

        // then
        var updated = articleCommentCountRepository.findById(articleId).orElseThrow();
        assertThat(updated.commentCount()).isEqualTo(0L);
    }

}
