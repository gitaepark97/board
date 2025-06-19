package board.backend.view.application;

import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleViewCreator {

    private final ArticleViewCountRepository articleViewCountRepository;

    void createCount(Long articleId) {
        articleViewCountRepository.save(ArticleViewCount.init(articleId));
    }
    
}
