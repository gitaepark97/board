package board.backend.like.application;

import board.backend.common.infra.CachedRepository;
import board.backend.like.domain.ArticleLikeCount;
import board.backend.like.infra.ArticleLikeCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NamedInterface
@RequiredArgsConstructor
@Component
public class ArticleLikeReader {

    private final static Duration CACHE_TTL = Duration.ofMinutes(1);

    private final CachedRepository<ArticleLikeCount, Long> cachedArticleLikeCountRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    public Map<Long, Long> count(List<Long> articleIds) {
        // 캐시 조회
        List<ArticleLikeCount> cached = cachedArticleLikeCountRepository.finalAllByKey(articleIds);

        Map<Long, Long> map = cached.stream()
            .collect(Collectors.toMap(ArticleLikeCount::getArticleId, ArticleLikeCount::getLikeCount));

        // 캐시 미스만 조회
        List<Long> missed = articleIds.stream()
            .filter(id -> !map.containsKey(id))
            .toList();
        if (!missed.isEmpty()) {
            List<ArticleLikeCount> uncached = articleLikeCountRepository.findAllById(missed);

            // 캐시에 저장
            uncached.forEach(articleLikeCount -> cachedArticleLikeCountRepository.save(articleLikeCount.getArticleId(), articleLikeCount, CACHE_TTL));

            // 합쳐서 반환
            uncached.forEach(articleLikeCount -> map.put(articleLikeCount.getArticleId(), articleLikeCount.getLikeCount()));
        }

        return map;
    }

}
