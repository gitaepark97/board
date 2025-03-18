package example.hugo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ArticleLikeCountTest {

    @Test
    @DisplayName("게시글 좋아요 수 초기화에 성공합니다.")
    void initArticleLikeCount() {
        // given
        Long articleId = 1L;
        Long likeCount = 2L;

        // when
        ArticleLikeCount result = new ArticleLikeCount(articleId, likeCount);

        // then
        assertThat(articleId).isEqualTo(articleId);
        assertThat(likeCount).isEqualTo(likeCount);
    }

    @Test
    @DisplayName("게시글 좋아요 수 증가에 성공합니다.")
    void increaseArticleLikeCount() {
        // given
        ArticleLikeCount existArticleLikeCount = new ArticleLikeCount(1L, 2L);

        // when
        ArticleLikeCount increaseArticleLikeCount = existArticleLikeCount.increase();

        // then
        assertThat(increaseArticleLikeCount.articleId()).isEqualTo(existArticleLikeCount.articleId());
        assertThat(increaseArticleLikeCount.likeCount()).isEqualTo(existArticleLikeCount.likeCount() + 1);
    }

    @Test
    @DisplayName("게시글 좋아요 수 감소에 성공합니다.")
    void decreaseArticleLikeCount() {
        // given
        ArticleLikeCount existArticleLikeCount = new ArticleLikeCount(1L, 2L);

        // when
        ArticleLikeCount increaseArticleLikeCount = existArticleLikeCount.decrease();

        // then
        assertThat(increaseArticleLikeCount.articleId()).isEqualTo(existArticleLikeCount.articleId());
        assertThat(increaseArticleLikeCount.likeCount()).isEqualTo(existArticleLikeCount.likeCount() - 1);
    }

}