package board.backend.article.application;

import board.backend.article.application.port.ArticleRepository;
import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.common.infra.CachedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

@NamedInterface
@RequiredArgsConstructor
@Component
public class ArticleValidator {

    private final CachedRepository<Article, Long> cachedArticleRepository;
    private final ArticleRepository articleRepository;

    public void checkArticleExistsOrThrow(Long articleId) {
        if (cachedArticleRepository.findByKey(articleId).isEmpty() && !articleRepository.existsById(articleId)) {
            throw new ArticleNotFound();
        }
    }

}
