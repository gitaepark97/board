package board.backend.view.application;

import board.backend.view.domain.ArticleViewCount;
import board.backend.view.infra.ArticleViewCountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ArticleViewWriter {

    private final ArticleViewCountRepository articleViewCountRepository;

    @Transactional
    public void increaseCount(Long articleId) {
        long result = articleViewCountRepository.increase(articleId);
        if (result == 0) {
            articleViewCountRepository.save(ArticleViewCount.init(articleId));
        }
    }

    @Transactional
    public void deleteArticle(Long articleId) {
        articleViewCountRepository.deleteById(articleId);
    }

}
