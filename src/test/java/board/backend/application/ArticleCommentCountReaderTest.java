package board.backend.application;

import board.backend.domain.ArticleCommentCount;
import board.backend.repository.ArticleCommentCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleCommentCountReaderTest {

    private ArticleCommentCountRepository articleCommentCountRepository;
    private ArticleCommentCountReader articleCommentCountReader;

    @BeforeEach
    void setUp() {
        articleCommentCountRepository = mock(ArticleCommentCountRepository.class);
        articleCommentCountReader = new ArticleCommentCountReader(articleCommentCountRepository);
    }

    @Test
    @DisplayName("게시글 ID 목록에 대한 댓글 수를 Map 형태로 반환한다")
    void count_returnsCommentCountMap() {
        // given
        List<Long> articleIds = List.of(1L, 2L, 3L);
        List<ArticleCommentCount> commentCounts = List.of(
            ArticleCommentCount.init(1L),
            ArticleCommentCount.init(2L)
        );

        when(articleCommentCountRepository.findByArticleIdIn(articleIds)).thenReturn(commentCounts);

        // when
        Map<Long, Long> result = articleCommentCountReader.count(articleIds);

        // then
        assertThat(result).containsEntry(1L, 1L)
            .containsEntry(2L, 1L)
            .doesNotContainKey(3L);
    }

    @Test
    @DisplayName("댓글 수 결과가 없으면 빈 Map을 반환한다")
    void count_emptyResult_returnsEmptyMap() {
        // given
        List<Long> articleIds = List.of(10L, 20L);
        when(articleCommentCountRepository.findByArticleIdIn(articleIds)).thenReturn(List.of());

        // when
        Map<Long, Long> result = articleCommentCountReader.count(articleIds);

        // then
        assertThat(result).isEmpty();
    }

}