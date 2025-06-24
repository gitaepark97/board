package board.backend.like.application;

import board.backend.common.cache.infra.CachedRepository;
import board.backend.like.application.port.ArticleLikeCountRepository;
import board.backend.like.domain.ArticleLikeCount;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static board.backend.like.application.LikeConstants.ARTICLE_LIKE_CACHE_TTL;
import static java.util.function.Predicate.not;

@NamedInterface
@RequiredArgsConstructor
@Component
public class ArticleLikeCounter {

    private final CachedRepository<ArticleLikeCount, Long> cachedArticleLikeCountRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    public Long count(Long articleId) {
        return cachedArticleLikeCountRepository.findByKey(articleId)
            .or(() -> articleLikeCountRepository.findById(articleId).map(articleLikeCount -> {
                cachedArticleLikeCountRepository.save(
                    articleLikeCount.getArticleId(),
                    articleLikeCount,
                    ARTICLE_LIKE_CACHE_TTL
                );
                return articleLikeCount;
            }))
            .map(ArticleLikeCount::getCount)
            .orElse(0L);
    }

    public Map<Long, Long> count(List<Long> articleIds) {
        // 캐시 조회
        List<ArticleLikeCount> cached = cachedArticleLikeCountRepository.findAllByKey(articleIds);

        Map<Long, Long> map = cached.stream()
            .collect(Collectors.toMap(ArticleLikeCount::getArticleId, ArticleLikeCount::getCount));

        // 캐시 미스만 조회
        List<Long> missed = articleIds.stream()
            .filter(not(map::containsKey))
            .peek(id -> map.put(id, 0L))
            .toList();
        if (!missed.isEmpty()) {
            List<ArticleLikeCount> uncached = articleLikeCountRepository.findAllById(missed);

            // 캐시에 저장
            uncached.forEach(articleLikeCount -> {
                cachedArticleLikeCountRepository.save(articleLikeCount.getArticleId(), articleLikeCount, ARTICLE_LIKE_CACHE_TTL);
                map.put(articleLikeCount.getArticleId(), articleLikeCount.getCount());
            });
        }

        return map;
    }

}
