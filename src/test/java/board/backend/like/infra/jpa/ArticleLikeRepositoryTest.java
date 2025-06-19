package board.backend.like.infra.jpa;

import board.backend.common.infra.TestRepository;
import board.backend.like.application.port.ArticleLikeRepository;
import board.backend.like.domain.ArticleLike;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ArticleLikeRepositoryImpl.class)
class ArticleLikeRepositoryTest extends TestRepository {

    private final Long articleId = 1L;
    private final Long userId = 100L;
    @Autowired
    private ArticleLikeRepository articleLikeRepository;

    @BeforeEach
    void setUp() {
        ArticleLike like = ArticleLike.create(articleId, userId, LocalDateTime.now());
        articleLikeRepository.save(like);
    }

    @Test
    @DisplayName("articleId와 userId로 좋아요가 존재하면 true를 반환한다")
    void existsByArticleIdAndUserId_success_whenExists_returnsTrue() {
        // when
        boolean result = articleLikeRepository.existsByArticleIdAndUserId(articleId, userId);

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