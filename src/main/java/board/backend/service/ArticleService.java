package board.backend.service;

import board.backend.common.IdProvider;
import board.backend.common.TimeProvider;
import board.backend.domain.Article;
import board.backend.domain.ArticleNotFound;
import board.backend.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final ArticleRepository articleRepository;

    public Article create(Long boardId, Long writerId, String title, String content) {
        // 게시글 생성
        Article newArticle = Article.create(idProvider.nextId(), boardId, writerId, title, content, timeProvider.now());
        // 게시글 저장
        articleRepository.save(newArticle);

        return newArticle;
    }

    public Article read(Long articleId) {
        // 게시글 조회
        return articleRepository.findById(articleId).orElseThrow(ArticleNotFound::new);
    }

    public Article update(Long articleId, String title, String content) {
        // 게시글 조회
        Article article = this.read(articleId);

        // 게시글 수정
        Article updatedArticle = article.update(title, content, timeProvider.now());
        // 게시글 저장
        articleRepository.save(updatedArticle);

        return updatedArticle;
    }

}
