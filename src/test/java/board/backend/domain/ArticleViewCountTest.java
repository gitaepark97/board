package board.backend.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleViewCountTest {

    @Test
    @DisplayName("게시글 조회 수 초기화에 성공한다")
    void init_success() {
        // given
        Long articleId = 1L;

        // when
        ArticleViewCount viewCount = ArticleViewCount.init(articleId);

        // then
        assertThat(viewCount.getArticleId()).isEqualTo(articleId);
        assertThat(viewCount.getViewCount()).isEqualTo(1L);
    }

}