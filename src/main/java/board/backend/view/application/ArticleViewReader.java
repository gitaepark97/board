package board.backend.view.application;

import board.backend.common.infra.CachedRepository;
import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@NamedInterface
@RequiredArgsConstructor
@Component
public class ArticleViewReader {

    private final static Duration CACHE_TTL = Duration.ofMinutes(1);

    private final CachedRepository<ArticleViewCount, Long> CachedArticleViewCountRepository;
    private final ArticleViewCountRepository articleViewCountRepository;

    public Long count(Long articleId) {
        return CachedArticleViewCountRepository.findByKey(articleId)
            .or(() -> articleViewCountRepository.findById(articleId))
            .map(ArticleViewCount::viewCount)
            .orElse(0L);
    }

    public Map<Long, Long> count(List<Long> articleIds) {
        // 캐시 조회
        List<ArticleViewCount> cached = CachedArticleViewCountRepository.finalAllByKey(articleIds);

        Map<Long, Long> map = cached.stream()
            .collect(Collectors.toMap(ArticleViewCount::articleId, ArticleViewCount::viewCount));

        // 캐시 미스만 조회
        List<Long> missed = articleIds.stream()
            .filter(not(map::containsKey))
            .toList();
        if (!missed.isEmpty()) {
            List<ArticleViewCount> uncached = articleViewCountRepository.findAllById(missed);

            // 캐시에 저장
            uncached.forEach(articleViewCount -> CachedArticleViewCountRepository.save(articleViewCount.articleId(), articleViewCount, CACHE_TTL));

            // 합쳐서 반환
            uncached.forEach(articleViewCount -> map.put(articleViewCount.articleId(), articleViewCount.viewCount()));
        }

        return map;
    }

}
