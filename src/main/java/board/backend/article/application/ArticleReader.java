package board.backend.article.application;

import board.backend.article.application.dto.ArticleWithWriterAndCounts;
import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.article.infra.ArticleCacheRepository;
import board.backend.article.infra.ArticleRepository;
import board.backend.comment.application.CommentReader;
import board.backend.like.application.ArticleLikeReader;
import board.backend.user.application.UserReader;
import board.backend.user.domain.User;
import board.backend.view.application.ArticleViewReader;
import board.backend.view.application.ArticleViewWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class ArticleReader {

    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    private final ArticleCacheRepository articleCacheRepository;
    private final ArticleRepository articleRepository;
    private final UserReader userReader;
    private final ArticleLikeReader articleLikeReader;
    private final ArticleViewReader articleViewReader;
    private final CommentReader commentReader;
    private final ArticleViewWriter articleViewWriter;

    public void checkArticleExistsOrThrow(Long articleId) {
        if (!articleRepository.customExistsById(articleId)) {
            throw new ArticleNotFound();
        }
    }

    List<ArticleWithWriterAndCounts> readAll(Long boardId, Long pageSize, Long lastArticleId) {
        // 게시글 목록 조회
        List<Article> articles = lastArticleId == null ?
            articleRepository.findAllByBoardId(boardId, pageSize) :
            articleRepository.findAllByBoardId(boardId, pageSize, lastArticleId);
        List<Long> articleIds = articles.stream().map(Article::getId).toList();
        List<Long> writerIds = articles.stream().map(Article::getWriterId).toList();

        // 작성자 조회
        Map<Long, User> writerMap = userReader.readAll(writerIds);

        // 게시글 좋아요 수 조회
        Map<Long, Long> likeCountMap = articleLikeReader.count(articleIds);

        // 게시글 조회 수 조회
        Map<Long, Long> viewCountMap = articleViewReader.count(articleIds);

        // 게시글 댓글 수 조회
        Map<Long, Long> commentCountMap = commentReader.count(articleIds);

        return articles.stream()
            .map(article -> new ArticleWithWriterAndCounts(
                article,
                writerMap.get(article.getWriterId()),
                likeCountMap.getOrDefault(article.getId(), 0L),
                viewCountMap.getOrDefault(article.getId(), 0L),
                commentCountMap.getOrDefault(article.getId(), 0L))
            )
            .toList();
    }

    Article read(Long articleId, String ip) {
        // 게시글 조회
        Article article = read(articleId);

        // 조회수 증가
        articleViewWriter.increaseCount(articleId, ip);

        return article;
    }

    private Article read(Long articleId) {
        return articleCacheRepository.get(articleId)
            .orElseGet(() -> {
                Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFound::new);
                articleCacheRepository.set(article, CACHE_TTL);
                return article;
            });
    }

}
