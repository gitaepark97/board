package board.backend.view.application;

import board.backend.view.domain.ArticleViewCount;
import board.backend.view.infra.ArticleViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ArticleViewReader {

    private final ArticleViewCountRepository articleViewCountRepository;

    public Map<Long, Long> count(List<Long> articleIds) {
        return articleViewCountRepository.findAllById(articleIds)
            .stream()
            .collect(Collectors.toMap(ArticleViewCount::getArticleId, ArticleViewCount::getViewCount));
    }

}
