package board.backend.application;

import board.backend.domain.Article;
import board.backend.domain.ArticleWithLikeCount;
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
    private final ArticleWriter articleWriter;

    public List<ArticleWithLikeCount> readAll(Long boardId, Long pageSize, Long lastArticleId) {
        // 게시글 목록 조회
        List<Article> articles = articleReader.readAll(boardId, pageSize, lastArticleId);

        // 게시글 좋아요 수 조회
        List<Long> articleIds = articles.stream().map(Article::getId).toList();
        Map<Long, Long> countMap = articleLikeReader.count(articleIds);

        return articles.stream()
            .map(article -> new ArticleWithLikeCount(article, countMap.getOrDefault(article.getId(), 0L)))
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
