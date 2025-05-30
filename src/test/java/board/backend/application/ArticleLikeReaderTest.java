package board.backend.application;

import board.backend.domain.ArticleLikeCount;
import board.backend.repository.ArticleLikeCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleLikeReaderTest {

    private ArticleLikeCountRepository articleLikeCountRepository;
    private ArticleLikeReader articleLikeReader;

    @BeforeEach
    void setUp() {
        articleLikeCountRepository = mock(ArticleLikeCountRepository.class);
        articleLikeReader = new ArticleLikeReader(articleLikeCountRepository);
    }

    @Test
    @DisplayName("게시글 ID 목록에 대한 좋아요 수를 Map 형태로 반환한다")
    void count_returnsLikeCountMap() {
        // given
        List<Long> articleIds = List.of(1L, 2L, 3L);
        List<ArticleLikeCount> likeCounts = List.of(
            ArticleLikeCount.init(1L),
            ArticleLikeCount.init(2L)
        );

        when(articleLikeCountRepository.findByArticleIdIn(articleIds)).thenReturn(likeCounts);

        // when
        Map<Long, Long> result = articleLikeReader.count(articleIds);

        // then
        assertThat(result).containsEntry(1L, 1L)
            .containsEntry(2L, 1L)
            .doesNotContainKey(3L);
    }

    @Test
    @DisplayName("결과가 없으면 빈 Map을 반환한다")
    void count_emptyResult_returnsEmptyMap() {
        // given
        List<Long> articleIds = List.of(10L, 20L);
        when(articleLikeCountRepository.findByArticleIdIn(articleIds)).thenReturn(List.of());

        // when
        Map<Long, Long> result = articleLikeReader.count(articleIds);

        // then
        assertThat(result).isEmpty();
    }


}