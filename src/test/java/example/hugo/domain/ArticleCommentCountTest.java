package example.hugo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ArticleCommentCountTest {

    @Test
    @DisplayName("게시글 댓글 초기화에 성공합니다.")
    void initArticleCommentCount() {
        // given
        Long articleId = 1L;
        Long commentCount = 2L;

        // when
        ArticleCommentCount result = ArticleCommentCount.init(articleId, commentCount);

        // then
        assertThat(result.articleId()).isEqualTo(articleId);
        assertThat(result.commentCount()).isEqualTo(commentCount);
    }

}