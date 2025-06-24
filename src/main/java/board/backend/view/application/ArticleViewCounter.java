package board.backend.view.application;

import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NamedInterface
@RequiredArgsConstructor
@Component
public class ArticleViewCounter {

    private final ArticleViewCountRepository articleViewCountRepository;

    public Long count(Long articleId) {
        return articleViewCountRepository.findById(articleId)
            .map(ArticleViewCount::getCount)
            .orElse(0L);
    }

    public Map<Long, Long> count(List<Long> articleIds) {
        Map<Long, Long> map = articleIds.stream()
            .collect(Collectors.toMap(id -> id, id -> 0L));

        articleViewCountRepository.findAllById(articleIds)
            .forEach(viewCount -> map.put(viewCount.getArticleId(), viewCount.getCount()));

        return map;
    }

}
