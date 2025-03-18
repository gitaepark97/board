package example.hugo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ArticleViewCountTest {

    @Test
    @DisplayName("게시글 조회 수 초기화에 성공합니다.")
    void initArticleViewCount() {
        // given
        Long articleId = 1L;

        // when
        ArticleViewCount result = ArticleViewCount.init(articleId);

        // then
        assertThat(result.articleId()).isEqualTo(articleId);
        assertThat(result.viewCount()).isEqualTo(1L);
    }

}