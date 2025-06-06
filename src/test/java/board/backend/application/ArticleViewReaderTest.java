package board.backend.application;

import board.backend.domain.ArticleViewCount;
import board.backend.infra.ArticleViewCountRepository;
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
    @DisplayName("게시글 ID 목록에 대한 조회수를 Map 형태로 반환한다")
    void count_returnsViewCountMap() {
        // given
        List<Long> articleIds = List.of(1L, 2L);
        List<ArticleViewCount> viewCounts = List.of(
            ArticleViewCount.init(1L),
            ArticleViewCount.init(2L)
        );

        when(articleViewCountRepository.findAllById(articleIds)).thenReturn(viewCounts);

        // when
        Map<Long, Long> result = articleViewReader.count(articleIds);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(1L)).isEqualTo(1L);
        assertThat(result.get(2L)).isEqualTo(1L);
    }

    @Test
    @DisplayName("조회수가 없는 게시글 ID 목록이면 빈 Map을 반환한다")
    void count_returnsEmptyMapWhenNoResult() {
        // given
        List<Long> articleIds = List.of(100L, 200L);
        when(articleViewCountRepository.findAllById(articleIds)).thenReturn(List.of());

        // when
        Map<Long, Long> result = articleViewReader.count(articleIds);

        // then
        assertThat(result).isEmpty();
    }

}