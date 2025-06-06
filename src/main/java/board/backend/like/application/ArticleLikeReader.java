package board.backend.like.application;

import board.backend.like.domain.ArticleLikeCount;
import board.backend.like.infra.ArticleLikeCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ArticleLikeReader {

    private final ArticleLikeCountRepository articleLikeCountRepository;

    public Map<Long, Long> count(List<Long> articleIds) {
        return articleLikeCountRepository.findAllById(articleIds)
            .stream()
            .collect(Collectors.toMap(ArticleLikeCount::getArticleId, ArticleLikeCount::getLikeCount));
    }

}
