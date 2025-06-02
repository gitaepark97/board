package board.backend.application;

import board.backend.domain.Article;
import board.backend.domain.ArticleNotFound;
import board.backend.infra.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class ArticleReader {

    private final ArticleRepository articleRepository;

    List<Article> readAll(Long boardId, Long pageSize, Long lastArticleId) {
        // 게시글 목록 조회
        return lastArticleId == null ?
            articleRepository.findAllByBoardId(boardId, pageSize) :
            articleRepository.findAllByBoardId(boardId, pageSize, lastArticleId);
    }

    Article read(Long articleId) {
        // 게시글 조회
        return articleRepository.findById(articleId).orElseThrow(ArticleNotFound::new);
    }

    void checkArticleExistOrThrow(Long articleId) {
        if (!articleRepository.customExistsById(articleId)) {
            throw new ArticleNotFound();
        }
    }

}
