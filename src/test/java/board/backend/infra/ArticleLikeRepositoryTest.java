package board.backend.infra;

import board.backend.TestcontainersConfiguration;
import board.backend.domain.ArticleLike;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("test")
@Import({
    TestcontainersConfiguration.class,
    QueryDSLConfig.class,
    CustomArticleLikeRepositoryImpl.class
})
@DataJpaTest
class ArticleLikeRepositoryTest {

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
    @DisplayName("articleId와 userId로 좋아요 존재 여부를 확인한다 - 존재함")
    void existsByArticleIdAndUserId_exists() {
        // when
        boolean result = articleLikeRepository.existsByArticleIdAndUserId(articleId, userId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("articleId와 userId로 좋아요 존재 여부를 확인한다 - 존재하지 않음")
    void existsByArticleIdAndUserId_notExists() {
        // when
        boolean result = articleLikeRepository.existsByArticleIdAndUserId(articleId, 999L);

        // then
        assertThat(result).isFalse();
    }

}