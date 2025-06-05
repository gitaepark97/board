package board.backend.application;

import board.backend.domain.ArticleLikeCount;
import board.backend.infra.ArticleLikeCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class ArticleLikeReader {

    private final ArticleLikeCountRepository articleLikeCountRepository;

    Map<Long, Long> count(List<Long> articleIds) {
        return articleLikeCountRepository.findAllById(articleIds)
            .stream()
            .collect(Collectors.toMap(ArticleLikeCount::getArticleId, ArticleLikeCount::getLikeCount));
    }

}
