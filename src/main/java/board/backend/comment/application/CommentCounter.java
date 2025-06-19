package board.backend.comment.application;

import board.backend.comment.application.port.ArticleCommentCountRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.common.infra.CachedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static board.backend.comment.application.CommentConstants.COMMENT_COUNT_CACHE_TTL;
import static java.util.function.Predicate.not;

@NamedInterface
@RequiredArgsConstructor
@Component
public class CommentCounter {

    private final CachedRepository<ArticleCommentCount, Long> cachedArticleCommentCountRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;

    public Long count(Long articleId) {
        return cachedArticleCommentCountRepository.findByKey(articleId)
            .or(() -> articleCommentCountRepository.findById(articleId)
                .map(articleCommentCount -> {
                    cachedArticleCommentCountRepository.save(
                        articleCommentCount.articleId(),
                        articleCommentCount,
                        COMMENT_COUNT_CACHE_TTL
                    );
                    return articleCommentCount;
                })
            )
            .map(ArticleCommentCount::commentCount)
            .orElse(0L);
    }

    public Map<Long, Long> count(List<Long> articleIds) {
        // 캐시 조회
        List<ArticleCommentCount> cached = cachedArticleCommentCountRepository.findAllByKey(articleIds);

        Map<Long, Long> map = cached.stream()
            .collect(Collectors.toMap(ArticleCommentCount::articleId, ArticleCommentCount::commentCount));

        // 캐시 미스만 조회
        List<Long> missed = articleIds.stream()
            .filter(not(map::containsKey))
            .peek(id -> map.put(id, 0L))
            .toList();
        if (!missed.isEmpty()) {
            List<ArticleCommentCount> uncached = articleCommentCountRepository.findAllById(missed);

            // 캐시에 저장
            uncached.forEach(articleCommentCount -> cachedArticleCommentCountRepository.save(articleCommentCount.articleId(), articleCommentCount, COMMENT_COUNT_CACHE_TTL));

            // 합쳐서 반환
            uncached.forEach(articleCommentCount -> map.put(articleCommentCount.articleId(), articleCommentCount.commentCount()));
        }

        return map;
    }

}
