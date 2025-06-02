package board.backend.application;

import board.backend.application.dto.ArticleWithCounts;
import board.backend.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleReader articleReader;
    private final ArticleLikeReader articleLikeReader;
    private final ArticleCommentCountReader articleCommentCountReader;
    private final ArticleWriter articleWriter;

    public List<ArticleWithCounts> readAll(Long boardId, Long pageSize, Long lastArticleId) {
        // 게시글 목록 조회
        List<Article> articles = articleReader.readAll(boardId, pageSize, lastArticleId);
        List<Long> articleIds = articles.stream().map(Article::getId).toList();

        // 게시글 좋아요 수 조회
        Map<Long, Long> likeCountMap = articleLikeReader.count(articleIds);

        // 게시글 댓글 수 조회
        Map<Long, Long> commentCountMap = articleCommentCountReader.count(articleIds);

        return articles.stream()
            .map(article -> new ArticleWithCounts(
                article,
                likeCountMap.getOrDefault(article.getId(), 0L),
                commentCountMap.getOrDefault(article.getId(), 0L))
            )
            .collect(Collectors.toList());
    }

    public Article read(Long articleId) {
        return articleReader.read(articleId);
    }

    public Article create(Long boardId, Long writerId, String title, String content) {
        return articleWriter.create(boardId, writerId, title, content);
    }

    public Article update(Long articleId, String title, String content) {
        return articleWriter.update(articleId, title, content);
    }

    public void delete(Long articleId) {
        articleWriter.delete(articleId);
    }

}
