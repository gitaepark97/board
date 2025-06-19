package board.backend.view.application;

import board.backend.view.application.port.ArticleViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@NamedInterface
@RequiredArgsConstructor
@Component
public class ArticleViewCounter {

    private final ArticleViewCountRepository articleViewCountRepository;

    public Long count(Long articleId) {
        return articleViewCountRepository.findById(articleId);
    }

    public Map<Long, Long> count(List<Long> articleIds) {
        return articleViewCountRepository.findAllById(articleIds);
    }

}
