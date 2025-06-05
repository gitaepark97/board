package board.backend.application;

import board.backend.application.dto.ArticleWithWriterAndCounts;
import board.backend.domain.Article;
import board.backend.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleReader articleReader;
    private final UserReader userReader;
    private final ArticleLikeReader articleLikeReader;
    private final ArticleCommentCountReader articleCommentCountReader;
    private final ArticleWriter articleWriter;
    private final ArticleLikeWriter articleLikeWriter;
    private final CommentWriter commentWriter;

    public List<ArticleWithWriterAndCounts> readAll(Long boardId, Long pageSize, Long lastArticleId) {
        // 게시글 목록 조회
        List<Article> articles = articleReader.readAll(boardId, pageSize, lastArticleId);
        List<Long> articleIds = articles.stream().map(Article::getId).toList();
        List<Long> writerIds = articles.stream().map(Article::getWriterId).toList();

        // 작성자 조회
        Map<Long, User> writerMap = userReader.readAll(writerIds);

        // 게시글 좋아요 수 조회
        Map<Long, Long> likeCountMap = articleLikeReader.count(articleIds);

        // 게시글 댓글 수 조회
        Map<Long, Long> commentCountMap = articleCommentCountReader.count(articleIds);

        return articles.stream()
            .map(article -> new ArticleWithWriterAndCounts(
                article,
                writerMap.get(article.getWriterId()),
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

    public Article update(Long articleId, Long userId, String title, String content) {
        return articleWriter.update(articleId, userId, title, content);
    }

    @Transactional
    public void delete(Long articleId, Long userId) {
        // 게시글 삭제
        articleWriter.delete(articleId, userId);

        // 게시글 좋아요 삭제
        articleLikeWriter.deleteArticle(articleId);

        // 게시글 댓글 삭제
        commentWriter.deleteArticle(articleId);
    }

}
