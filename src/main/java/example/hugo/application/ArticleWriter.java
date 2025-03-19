package example.hugo.application;

import example.hugo.domain.Article;
import example.hugo.domain.BoardArticleCount;
import example.hugo.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
class ArticleWriter {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final ArticleRepository articleRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;

    @Transactional
    Article createArticle(String title, String content, Long boardId, Long writerId) {
        // 게시글 생성
        Article newArticle = Article.create(idProvider.nextId(), title, content, boardId, writerId, timeProvider.now());
        articleRepository.save(newArticle);

        // 게시판 게시글 수 변경
        long result = boardArticleCountRepository.increase(newArticle.boardId());
        if (result == 0L) {
            boardArticleCountRepository.save(BoardArticleCount.init(newArticle.boardId()));
        }

        return newArticle;
    }

    @Transactional
    Article updateArticle(Long articleId, String title, String content) {
        // 기존 게시글 조회
        Article existArticle = articleRepository.findById(articleId)
            .orElseThrow(ErrorCode.NOT_FOUND_ARTICLE::toException);

        // 게시글 변경
        Article updatedArticle = existArticle.update(title, content, timeProvider.now());
        articleRepository.save(updatedArticle);

        return updatedArticle;
    }

    @Transactional
    void deleteArticle(Long articleId) {
        // 기존 게시글 조회
        articleRepository.findById(articleId).ifPresent(existArticle -> {
            // 게시글 삭제
            articleRepository.deleteById(existArticle.articleId());

            // 게시판 게시글 수 변경
            boardArticleCountRepository.decrease(existArticle.boardId());
        });
    }

}
