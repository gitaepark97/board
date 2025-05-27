package board.backend.service;

import board.backend.domain.Article;
import board.backend.domain.ArticleNotFound;
import board.backend.repository.ArticleRepository;
import board.backend.support.IdProvider;
import board.backend.support.TimeProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final ArticleRepository articleRepository;

    public List<Article> readAll(Long boardId, Long pageSize, Long lastArticleId) {
        // 게시글 목록 조회
        return lastArticleId == null ?
            articleRepository.findAllByBoardId(boardId, pageSize) :
            articleRepository.findAllByBoardId(boardId, pageSize, lastArticleId);
    }

    public Article read(Long articleId) {
        // 게시글 조회
        return articleRepository.findById(articleId).orElseThrow(ArticleNotFound::new);
    }

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
        Article article = this.read(articleId);

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

    void checkArticleExistOrThrow(Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            throw new ArticleNotFound();
        }
    }

}
