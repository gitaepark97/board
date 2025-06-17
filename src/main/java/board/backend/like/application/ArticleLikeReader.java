package board.backend.like.application;

import board.backend.common.infra.CachedRepository;
import board.backend.like.application.port.ArticleLikeCountRepository;
import board.backend.like.domain.ArticleLikeCount;
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
public class ArticleLikeReader {

    private final static Duration CACHE_TTL = Duration.ofMinutes(1);

    private final CachedRepository<ArticleLikeCount, Long> cachedArticleLikeCountRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    public Long count(Long articleId) {
        return cachedArticleLikeCountRepository.findByKey(articleId)
            .or(() -> articleLikeCountRepository.findById(articleId))
            .map(ArticleLikeCount::likeCount)
            .orElse(0L);
    }

    public Map<Long, Long> count(List<Long> articleIds) {
        // 캐시 조회
        List<ArticleLikeCount> cached = cachedArticleLikeCountRepository.findAllByKey(articleIds);

        Map<Long, Long> map = cached.stream()
            .collect(Collectors.toMap(ArticleLikeCount::articleId, ArticleLikeCount::likeCount));

        // 캐시 미스만 조회
        List<Long> missed = articleIds.stream()
            .filter(not(map::containsKey))
            .peek(id -> map.put(id, 0L))
            .toList();
        if (!missed.isEmpty()) {
            List<ArticleLikeCount> uncached = articleLikeCountRepository.findAllById(missed);

            // 캐시에 저장
            uncached.forEach(articleLikeCount -> cachedArticleLikeCountRepository.save(articleLikeCount.articleId(), articleLikeCount, CACHE_TTL));

            // 합쳐서 반환
            uncached.forEach(articleLikeCount -> map.put(articleLikeCount.articleId(), articleLikeCount.likeCount()));
        }

        return map;
    }

}
