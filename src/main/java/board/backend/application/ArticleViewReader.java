package board.backend.application;

import board.backend.domain.ArticleViewCount;
import board.backend.infra.ArticleViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class ArticleViewReader {

    private final ArticleViewCountRepository articleViewCountRepository;

    Map<Long, Long> count(List<Long> articleIds) {
        return articleViewCountRepository.findAllById(articleIds)
            .stream()
            .collect(Collectors.toMap(ArticleViewCount::getArticleId, ArticleViewCount::getViewCount));
    }

}
