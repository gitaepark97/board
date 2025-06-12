package board.backend.articleRead.application;

import board.backend.article.application.ArticleReader;
import board.backend.article.domain.Article;
import board.backend.articleRead.application.dto.ArticleWithWriterAndCounts;
import board.backend.comment.application.CommentReader;
import board.backend.like.application.ArticleLikeReader;
import board.backend.user.application.UserReader;
import board.backend.user.domain.User;
import board.backend.view.application.ArticleViewReader;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ArticleReadService {

    private final ArticleReader articleReader;
    private final UserReader userReader;
    private final ArticleLikeReader articleLikeReader;
    private final ArticleViewReader articleViewReader;
    private final CommentReader commentReader;

    @Cacheable(value = "articleList::board", key = "#boardId", condition = "#pageSize.equals(10L) && #lastArticleId == null")
    public List<ArticleWithWriterAndCounts> readAll(Long boardId, Long pageSize, Long lastArticleId) {
        // 게시글 목록 조회
        List<Article> articles = articleReader.readAll(boardId, pageSize, lastArticleId);
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
            .collect(Collectors.toList());
    }

}
