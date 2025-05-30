package board.backend.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ArticleCommentCountTest {

    @Test
    @DisplayName("게시글 댓글 수 초기화에 성공한다")
    void init_success() {
        // given
        Long articleId = 1L;

        // when
        ArticleCommentCount commentCount = ArticleCommentCount.init(articleId);

        // then
        assertThat(commentCount.getArticleId()).isEqualTo(articleId);
        assertThat(commentCount.getCommentCount()).isEqualTo(1L);
    }

}