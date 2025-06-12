package board.backend.view.application;

import board.backend.common.infra.CachedRepository;
import board.backend.view.domain.ArticleViewCount;
import board.backend.view.infra.ArticleViewCountRepository;
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
public class ArticleViewReader {

    private final static Duration CACHE_TTL = Duration.ofMinutes(1);

    private final CachedRepository<ArticleViewCount, Long> CachedArticleViewCountRepository;
    private final ArticleViewCountRepository articleViewCountRepository;

    public Map<Long, Long> count(List<Long> articleIds) {
        // 캐시 조회
        List<ArticleViewCount> cached = CachedArticleViewCountRepository.finalAllByKey(articleIds);

        Map<Long, Long> map = cached.stream()
            .collect(Collectors.toMap(ArticleViewCount::getArticleId, ArticleViewCount::getViewCount));

        // 캐시 미스만 조회
        List<Long> missed = articleIds.stream()
            .filter(id -> !map.containsKey(id))
            .toList();
        if (!missed.isEmpty()) {
            List<ArticleViewCount> uncached = articleViewCountRepository.findAllById(missed);

            // 캐시에 저장
            uncached.forEach(articleViewCount -> CachedArticleViewCountRepository.save(articleViewCount.getArticleId(), articleViewCount, CACHE_TTL));

            // 합쳐서 반환
            uncached.forEach(articleViewCount -> map.put(articleViewCount.getArticleId(), articleViewCount.getViewCount()));
        }

        return map;
    }

}
