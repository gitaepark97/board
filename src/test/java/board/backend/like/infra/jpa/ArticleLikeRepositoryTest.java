package board.backend.like.infra.jpa;

import board.backend.common.infra.TestJpaRepository;
import board.backend.like.application.port.ArticleLikeRepository;
import board.backend.like.domain.ArticleLike;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ArticleLikeRepositoryImpl.class)
class ArticleLikeRepositoryTest extends TestJpaRepository {


    private final Long articleId = 1L;

    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    @Test
    @DisplayName("articleId와 userId로 좋아요가 존재하면 true를 반환한다")
    void existsByArticleIdAndUserId_success_whenExists_returnsTrue() {
        // given
        ArticleLike like = ArticleLike.create(articleId, 100L, LocalDateTime.now());
        articleLikeRepository.save(like);

        // when
        boolean result = articleLikeRepository.existsByArticleIdAndUserId(like.articleId(), like.userId());

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("articleId와 userId로 좋아요가 존재하지 않으면 false를 반환한다")
    void existsByArticleIdAndUserId_success_whenNotExists_returnsFalse() {
        // when
        boolean result = articleLikeRepository.existsByArticleIdAndUserId(articleId, 999L);

        // then
        assertThat(result).isFalse();
    }

}