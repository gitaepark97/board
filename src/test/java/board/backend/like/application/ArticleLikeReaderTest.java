package board.backend.like.application;

import board.backend.common.infra.CachedRepository;
import board.backend.like.domain.ArticleLikeCount;
import board.backend.like.infra.ArticleLikeCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleLikeReaderTest {

    private CachedRepository<ArticleLikeCount, Long> articleLikeCountLongCachedRepository;
    private ArticleLikeCountRepository articleLikeCountRepository;
    private ArticleLikeReader articleLikeReader;

    @BeforeEach
    void setUp() {
        articleLikeCountLongCachedRepository = mock(CachedRepository.class);
        articleLikeCountRepository = mock(ArticleLikeCountRepository.class);
        articleLikeReader = new ArticleLikeReader(articleLikeCountLongCachedRepository, articleLikeCountRepository);
    }

    @Test
    @DisplayName("캐시에 존재하는 좋아요 수는 DB 조회 없이 반환한다")
    void count_allCacheHit_success() {
        // given
        List<Long> articleIds = List.of(1L, 2L);
        List<ArticleLikeCount> cached = List.of(
            ArticleLikeCount.init(1L),
            ArticleLikeCount.init(2L)
        );
        when(articleLikeCountLongCachedRepository.finalAllByKey(articleIds)).thenReturn(cached);

        // when
        Map<Long, Long> result = articleLikeReader.count(articleIds);

        // then
        assertThat(result).containsEntry(1L, 1L).containsEntry(2L, 1L);
    }

    @Test
    @DisplayName("일부 캐시 미스 시 DB에서 조회하고, 캐시에 저장한 뒤 합쳐서 반환한다")
    void count_partialCacheMiss_success() {
        // given
        List<Long> articleIds = List.of(1L, 2L, 3L);
        List<ArticleLikeCount> cached = List.of(
            ArticleLikeCount.init(1L),
            ArticleLikeCount.init(3L)
        );
        List<ArticleLikeCount> uncached = List.of(
            ArticleLikeCount.init(2L)
        );
        when(articleLikeCountLongCachedRepository.finalAllByKey(articleIds)).thenReturn(cached);
        when(articleLikeCountRepository.findAllById(List.of(2L))).thenReturn(uncached);

        // when
        Map<Long, Long> result = articleLikeReader.count(articleIds);

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
        List<ArticleLikeCount> fromDb = List.of(
            ArticleLikeCount.init(1L),
            ArticleLikeCount.init(2L)
        );
        when(articleLikeCountLongCachedRepository.finalAllByKey(articleIds)).thenReturn(List.of());
        when(articleLikeCountRepository.findAllById(articleIds)).thenReturn(fromDb);

        // when
        Map<Long, Long> result = articleLikeReader.count(articleIds);

        // then
        assertThat(result).containsEntry(1L, 1L).containsEntry(2L, 1L);
    }

}