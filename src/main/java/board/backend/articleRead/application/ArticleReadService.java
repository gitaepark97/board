package board.backend.articleRead.application;

import board.backend.article.application.ArticleReader;
import board.backend.article.domain.Article;
import board.backend.articleRead.application.dto.ArticleDetail;
import board.backend.comment.application.CommentReader;
import board.backend.hotArticle.application.HotArticleReader;
import board.backend.like.application.ArticleLikeReader;
import board.backend.user.application.UserReader;
import board.backend.user.domain.User;
import board.backend.view.application.ArticleViewReader;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ArticleReadService {

    private final ArticleReader articleReader;
    private final UserReader userReader;
    private final ArticleLikeReader articleLikeReader;
    private final ArticleViewReader articleViewReader;
    private final CommentReader commentReader;
    private final HotArticleReader hotArticleReader;

    @Cacheable(value = "article::list::board", key = "#boardId", condition = "#pageSize.equals(10L) && #lastArticleId == null")
    public List<ArticleDetail> readAll(Long boardId, Long pageSize, Long lastArticleId) {
        // 게시글 목록 조회
        List<Article> articles = articleReader.readAll(boardId, pageSize, lastArticleId);
        List<Long> articleIds = articles.stream().map(Article::id).toList();

        return mapToArticleDetails(articles, articleIds);
    }

    @Cacheable(value = "article::hot::list", key = "#dateStr")
    public List<ArticleDetail> readAllHot(String dateStr) {
        // 인기 게시글 ID 조회
        List<Long> articleIds = hotArticleReader.readAll(dateStr);

        // 게시글 조회
        List<Article> articles = articleReader.readAll(articleIds);

        return mapToArticleDetails(articles, articleIds);
    }

    public ArticleDetail read(Long articleId, String ip) {
        // 게시글 조회
        Article article = articleReader.read(articleId, ip);

        // 작성자 조회
        User writer = userReader.read(article.writerId());

        // 게시글 좋아요 수 조회
        Long likeCount = articleLikeReader.count(articleId);

        // 게시글 조회 수 조회
        Long viewCount = articleViewReader.count(articleId);
        
        // 게시글 댓글 수 조회
        Long commentCount = commentReader.count(articleId);

        return new ArticleDetail(article, writer, likeCount, viewCount, commentCount);
    }

    private List<ArticleDetail> mapToArticleDetails(List<Article> articles, List<Long> articleIds) {
        // 작성자 ID 조회
        List<Long> writerIds = articles.stream().map(Article::writerId).toList();

        // 작성자 조회
        Map<Long, User> writerMap = userReader.readAll(writerIds);

        // 게시글 좋아요 수 조회
        Map<Long, Long> likeCountMap = articleLikeReader.count(articleIds);

        // 게시글 조회 수 조회
        Map<Long, Long> viewCountMap = articleViewReader.count(articleIds);

        // 게시글 댓글 수 조회
        Map<Long, Long> commentCountMap = commentReader.count(articleIds);

        return articles.stream()
            .map(article -> new ArticleDetail(
                article,
                writerMap.get(article.writerId()),
                likeCountMap.get(article.id()),
                viewCountMap.get(article.id()),
                commentCountMap.get(article.id()))
            )
            .toList();
    }

}
