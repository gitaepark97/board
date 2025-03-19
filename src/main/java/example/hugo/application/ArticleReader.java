package example.hugo.application;

import example.hugo.domain.Article;
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

    Article readArticle(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(ErrorCode.NOT_FOUND_ARTICLE::toException);
    }

    List<Article> readArticles(Long boardId, Long pageSize, Long lastArticleId) {
        return lastArticleId == null ?
            articleRepository.findAllByBoardId(boardId, pageSize) :
            articleRepository.findAllByBoardId(boardId, pageSize, lastArticleId);
    }

    Long countBoardArticles(Long boardId) {
        return boardArticleCountRepository.findByBoardId(boardId)
            .map(BoardArticleCount::articleCount)
            .orElse(0L);
    }

}
