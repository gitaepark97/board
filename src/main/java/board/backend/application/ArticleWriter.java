package board.backend.application;

import board.backend.domain.Article;
import board.backend.domain.ArticleNotFound;
import board.backend.infra.ArticleRepository;
import board.backend.support.IdProvider;
import board.backend.support.TimeProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleWriter {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final ArticleRepository articleRepository;

    @Transactional
    public Article create(Long boardId, Long writerId, String title, String content) {
        // 게시글 생성
        Article newArticle = Article.create(idProvider.nextId(), boardId, writerId, title, content, timeProvider.now());
        // 게시글 저장
        articleRepository.save(newArticle);

        return newArticle;
    }

    @Transactional
    public Article update(Long articleId, String title, String content) {
        // 게시글 조회
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFound::new);

        // 게시글 수정
        Article updatedArticle = article.update(title, content, timeProvider.now());
        // 게시글 저장
        articleRepository.save(updatedArticle);

        return updatedArticle;
    }

    @Transactional
    public void delete(Long articleId) {
        // 게시글 삭제
        articleRepository.findById(articleId).ifPresent(articleRepository::delete);
    }

}
