package board.backend.view.application;

import board.backend.view.domain.ArticleViewCount;
import board.backend.view.infra.ArticleViewCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleViewReaderTest {

    private ArticleViewCountRepository articleViewCountRepository;
    private ArticleViewReader articleViewReader;

    @BeforeEach
    void setUp() {
        articleViewCountRepository = mock(ArticleViewCountRepository.class);
        articleViewReader = new ArticleViewReader(articleViewCountRepository);
    }

    @Test
    @DisplayName("게시글 ID 목록에 해당하는 조회수 목록을 반환한다")
    void count_success() {
        // given
        Long articleId1 = 1L;
        Long articleId2 = 2L;
        List<Long> articleIds = List.of(articleId1, articleId2);
        List<ArticleViewCount> viewCounts = List.of(
            ArticleViewCount.init(articleId1),
            ArticleViewCount.init(articleId2)
        );
        when(articleViewCountRepository.findAllById(articleIds)).thenReturn(viewCounts);

        // when
        Map<Long, Long> result = articleViewReader.count(articleIds);

        // then
        assertThat(result.get(articleId1)).isEqualTo(1L);
        assertThat(result.get(articleId2)).isEqualTo(1L);
    }

}