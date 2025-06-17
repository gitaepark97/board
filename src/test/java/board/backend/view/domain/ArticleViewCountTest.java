package board.backend.view.domain;

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
        assertThat(viewCount.articleId()).isEqualTo(articleId);
        assertThat(viewCount.viewCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("게시글 조회 수 생성에 성공한다")
    void create_success() {
        // given
        Long articleId = 1L;
        Long count = 2L;

        // when
        ArticleViewCount viewCount = ArticleViewCount.create(articleId, count);

        // then
        assertThat(viewCount.articleId()).isEqualTo(articleId);
        assertThat(viewCount.viewCount()).isEqualTo(count);
    }

}