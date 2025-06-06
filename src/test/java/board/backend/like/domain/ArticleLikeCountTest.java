package board.backend.like.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ArticleLikeCountTest {

    @Test
    @DisplayName("게시글 좋아요 수 초기화에 성공한다")
    void init_success() {
        // given
        Long articleId = 1L;

        // when
        ArticleLikeCount likeCount = ArticleLikeCount.init(articleId);

        // then
        assertThat(likeCount.getArticleId()).isEqualTo(articleId);
        assertThat(likeCount.getLikeCount()).isEqualTo(1L);
    }

}