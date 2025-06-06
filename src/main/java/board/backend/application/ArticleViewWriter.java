package board.backend.application;

import board.backend.domain.ArticleViewCount;
import board.backend.infra.ArticleViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleViewWriter {

    private final ArticleViewCountRepository articleViewCountRepository;

    void increase(Long articleId) {
        long result = articleViewCountRepository.increase(articleId);
        if (result == 0) {
            articleViewCountRepository.save(ArticleViewCount.init(articleId));
        }
    }
    
    void deleteArticle(Long articleId) {
        articleViewCountRepository.deleteById(articleId);
    }

}
