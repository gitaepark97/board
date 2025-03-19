package example.hugo.application;

import example.hugo.domain.Article;
import example.hugo.domain.ArticleViewCount;
import example.hugo.domain.BoardArticleCount;
import example.hugo.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class ArticleReader {

    private final ArticleRepository articleRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;
    private final ArticleViewCountRepository articleViewCountRepository;

    void checkArticleExists(Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            throw ErrorCode.NOT_FOUND_ARTICLE.toException();
        }
    }

    Article readArticle(Long articleId) {
        // 게시글 조회
        Article existingArticle = articleRepository.findById(articleId)
            .orElseThrow(ErrorCode.NOT_FOUND_ARTICLE::toException);

        // 게시글 조회 수 변경
        long result = articleViewCountRepository.increase(existingArticle.articleId());
        if (result == 0L) {
            articleViewCountRepository.save(ArticleViewCount.init(existingArticle.articleId()));
        }

        return existingArticle;
    }

    List<Article> readArticles(Long boardId, Long pageSize, Long lastArticleId) {
        return lastArticleId == null ?
            articleRepository.findAllByBoardId(boardId, pageSize) :
            articleRepository.findAllByBoardId(boardId, pageSize, lastArticleId);
    }

    Long countBoardArticles(Long boardId) {
        return boardArticleCountRepository.findById(boardId)
            .map(BoardArticleCount::articleCount)
            .orElse(0L);
    }

    Long countArticleViews(Long articleId) {
        return articleViewCountRepository.findById(articleId)
            .map(ArticleViewCount::viewCount)
            .orElse(0L);
    }

}
