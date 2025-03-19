package example.hugo.application;

import example.hugo.domain.ArticleLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArticleLikeService {

    private final ArticleLikeReader articleLikeReader;
    private final ArticleLikeWriter articleLikeWriter;
    private final ArticleReader articleReader;

    public ArticleLike readArticleLike(Long articleId, Long userId) {
        return articleLikeReader.readArticleLike(articleId, userId);
    }

    public Long countArticleLikes(Long articleId) {
        return articleLikeReader.countArticleLikes(articleId);
    }

    public void like(Long articleId, Long userId) {
        articleReader.checkArticleExists(articleId);
        articleLikeWriter.createArticleLike(articleId, userId);
    }

    public void unlike(Long articleId, Long userId) {
        articleLikeWriter.deleteArticleLike(articleId, userId);
    }

}
