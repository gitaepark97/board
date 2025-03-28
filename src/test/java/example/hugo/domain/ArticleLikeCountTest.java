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

        // when
        ArticleLikeCount result = ArticleLikeCount.init(articleId);

        // then
        assertThat(result.articleId()).isEqualTo(articleId);
        assertThat(result.likeCount()).isEqualTo(1L);
    }
    
}