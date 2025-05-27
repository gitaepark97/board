package board.backend.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ArticleLikeTest {

    @Test
    @DisplayName("게시글 좋아요 생성에 성공한다")
    void create_success() {
        // given
        Long id = 1L;
        Long articleId = 10L;
        Long userId = 100L;
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);

        // when
        ArticleLike articleLike = ArticleLike.like(articleId, userId, now);

        // then
        assertThat(articleLike.getId().articleId()).isEqualTo(articleId);
        assertThat(articleLike.getId().userId()).isEqualTo(userId);
        assertThat(articleLike.getCreatedAt()).isEqualTo(now);
    }

}