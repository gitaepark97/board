package board.backend.like.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleLikeCountTest {

    @Test
    @DisplayName("init 메서드는 articleId로 초기화된 ArticleLikeCount를 반환한다")
    void init_success_setsInitialLikeCountToOne() {
        // given
        Long articleId = 1L;

        // when
        ArticleLikeCount count = ArticleLikeCount.init(articleId);

        // then
        assertThat(count.getArticleId()).isEqualTo(articleId);
        assertThat(count.getCount()).isEqualTo(1L);
    }

}