package example.hugo.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BoardArticleCountTest {

    @Test
    @DisplayName("게시판 게시글 수 초기화에 성공합니다.")
    void initArticleViewCount() {
        // given
        Long boardId = 1L;

        // when
        BoardArticleCount result = BoardArticleCount.init(boardId);

        // then
        assertThat(result.boardId()).isEqualTo(boardId);
        assertThat(result.articleCount()).isEqualTo(1L);
    }

}