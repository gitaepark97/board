package board.backend.like.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleLikeTest {

    @Test
    @DisplayName("ArticleLike 생성 시 필드가 올바르게 설정된다")
    void create_success_setsAllFields() {
        // given
        Long articleId = 1L;
        Long userId = 10L;
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

        // when
        ArticleLike articleLike = ArticleLike.create(articleId, userId, now);

        // then
        assertThat(articleLike.articleId()).isEqualTo(articleId);
        assertThat(articleLike.userId()).isEqualTo(userId);
        assertThat(articleLike.createdAt()).isEqualTo(now);
    }

}