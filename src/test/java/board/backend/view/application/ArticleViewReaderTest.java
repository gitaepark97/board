package board.backend.view.application;

import board.backend.common.infra.CachedRepository;
import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleViewReaderTest {

    private CachedRepository<ArticleViewCount, Long> articleViewCountLongCachedRepository;
    private ArticleViewCountRepository articleViewCountRepository;
    private ArticleViewReader articleViewReader;

    @BeforeEach
    void setUp() {
        articleViewCountLongCachedRepository = mock(CachedRepository.class);
        articleViewCountRepository = mock(ArticleViewCountRepository.class);
        articleViewReader = new ArticleViewReader(articleViewCountLongCachedRepository, articleViewCountRepository);
    }

    @Test
    @DisplayName("캐시에 존재하는 조회 수는 DB 조회 없이 반환한다")
    void count_allCacheHit_success() {
        // given
        List<Long> articleIds = List.of(1L, 2L);
        List<ArticleViewCount> cached = List.of(
            ArticleViewCount.init(1L),
            ArticleViewCount.init(2L)
        );
        when(articleViewCountLongCachedRepository.finalAllByKey(articleIds)).thenReturn(cached);

        // when
        Map<Long, Long> result = articleViewReader.count(articleIds);

        // then
        assertThat(result).containsEntry(1L, 1L).containsEntry(2L, 1L);
    }

    @Test
    @DisplayName("일부 캐시 미스 시 DB에서 조회하고, 캐시에 저장한 뒤 합쳐서 반환한다")
    void count_partialCacheMiss_success() {
        // given
        List<Long> articleIds = List.of(1L, 2L, 3L);
        List<ArticleViewCount> cached = List.of(
            ArticleViewCount.init(1L),
            ArticleViewCount.init(3L)
        );
        List<ArticleViewCount> uncached = List.of(
            ArticleViewCount.init(2L)
        );
        when(articleViewCountLongCachedRepository.finalAllByKey(articleIds)).thenReturn(cached);
        when(articleViewCountRepository.findAllById(List.of(2L))).thenReturn(uncached);

        // when
        Map<Long, Long> result = articleViewReader.count(articleIds);

        // then
        assertThat(result).containsEntry(1L, 1L)
            .containsEntry(2L, 1L)
            .containsEntry(3L, 1L);
    }

    @Test
    @DisplayName("모두 캐시 미스일 경우 DB에서 모두 조회하고 캐시에 저장 후 반환한다")
    void count_allCacheMiss_success() {
        // given
        List<Long> articleIds = List.of(1L, 2L);
        List<ArticleViewCount> fromDb = List.of(
            ArticleViewCount.init(1L),
            ArticleViewCount.init(2L)
        );
        when(articleViewCountLongCachedRepository.finalAllByKey(articleIds)).thenReturn(List.of());
        when(articleViewCountRepository.findAllById(articleIds)).thenReturn(fromDb);

        // when
        Map<Long, Long> result = articleViewReader.count(articleIds);

        // then
        assertThat(result).containsEntry(1L, 1L).containsEntry(2L, 1L);
    }

}