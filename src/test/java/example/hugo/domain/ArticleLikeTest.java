package example.hugo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class ArticleLikeTest {

    @Test
    @DisplayName("게시글 좋아요 생성에 성공합니다.")
    void createArticleLike() {
        // given
        Long articleLikeId = 1L;
        Long articleId = 1L;
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();

        // when
        ArticleLike result = ArticleLike.create(articleLikeId, articleId, userId, now);

        // then
        assertThat(result.articleLikeId()).isEqualTo(articleLikeId);
        assertThat(result.articleId()).isEqualTo(articleId);
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.createdAt()).isEqualTo(now);
    }

}