package board.backend.application;

import board.backend.domain.ArticleCommentCount;
import board.backend.repository.ArticleCommentCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class ArticleCommentCountReader {

    private final ArticleCommentCountRepository articleCommentCountRepository;

    Map<Long, Long> count(List<Long> articleIds) {
        return articleCommentCountRepository.findByArticleIdIn(articleIds)
            .stream()
            .collect(Collectors.toMap(ArticleCommentCount::getArticleId, ArticleCommentCount::getCommentCount));
    }

}
