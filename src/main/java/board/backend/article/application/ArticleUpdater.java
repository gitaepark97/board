package board.backend.article.application;

import board.backend.article.application.port.ArticleRepository;
import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.common.cache.infra.CachedRepository;
import board.backend.common.support.TimeProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleUpdater {

    private final TimeProvider timeProvider;
    private final CachedRepository<Article, Long> articleCachedRepository;
    private final ArticleRepository articleRepository;

    @Transactional
    Article update(Long articleId, Long userId, String title, String content) {
        // 게시글 조회
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFound::new);
        // 게시글 캐시 삭제
        articleCachedRepository.delete(articleId);

        // 게시글 수정
        Article updatedArticle = article.update(userId, title, content, timeProvider.now());
        // 게시글 저장
        articleRepository.save(updatedArticle);

        return updatedArticle;
    }

}
