package board.backend.view.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleViewCountTest {

    @Test
    @DisplayName("init 메서드는 조회 수 0으로 ArticleViewCount를 생성한다")
    void init_success_initializesViewCountToZero() {
        // given
        Long articleId = 1L;

        // when
        ArticleViewCount viewCount = ArticleViewCount.init(articleId);

        // then
        assertThat(viewCount.articleId()).isEqualTo(articleId);
        assertThat(viewCount.viewCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("create 메서드는 주어진 값으로 ArticleViewCount를 생성한다")
    void create_success_createsWithGivenValues() {
        // given
        Long articleId = 2L;
        Long count = 123L;

        // when
        ArticleViewCount viewCount = ArticleViewCount.create(articleId, count);

        // then
        assertThat(viewCount.articleId()).isEqualTo(articleId);
        assertThat(viewCount.viewCount()).isEqualTo(count);
    }

}