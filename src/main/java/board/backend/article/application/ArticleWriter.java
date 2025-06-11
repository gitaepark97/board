package board.backend.article.application;

import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.article.infra.ArticleCacheRepository;
import board.backend.article.infra.ArticleRepository;
import board.backend.comment.application.CommentWriter;
import board.backend.common.support.IdProvider;
import board.backend.common.support.TimeProvider;
import board.backend.like.application.ArticleLikeWriter;
import board.backend.view.application.ArticleViewWriter;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleWriter {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final ArticleCacheRepository articleCacheRepository;
    private final ArticleRepository articleRepository;
    private final ArticleLikeWriter articleLikeWriter;
    private final ArticleViewWriter articleViewWriter;
    private final CommentWriter commentWriter;

    @Transactional
    public Article create(Long boardId, Long writerId, String title, String content) {
        // 게시글 생성
        Article newArticle = Article.create(idProvider.nextId(), boardId, writerId, title, content, timeProvider.now());
        // 게시글 저장
        articleRepository.save(newArticle);
        // 게시글 조회 수 저장
        articleViewWriter.saveCount(newArticle.getId());

        return newArticle;
    }

    @Transactional
    public Article update(Long articleId, Long userId, String title, String content) {
        // 게시글 조회
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFound::new);
        // 게시글 캐시 삭제
        articleCacheRepository.delete(articleId);

        // 게시글 수정
        Article updatedArticle = article.update(userId, title, content, timeProvider.now());
        // 게시글 저장
        articleRepository.save(updatedArticle);

        return updatedArticle;
    }

    @Transactional
    public void delete(Long articleId, Long userId) {
        // 게시글 삭제
        articleRepository.findById(articleId).ifPresent((article -> {
            // 작성자 확인
            article.checkIsWriter(userId);
            articleRepository.delete(article);
        }));

        // 게시글 좋아요 삭제
        articleLikeWriter.deleteArticle(articleId);

        // 게시글 조회수 삭제
        articleViewWriter.deleteArticle(articleId);

        // 게시글 댓글 삭제
        commentWriter.deleteArticle(articleId);
    }

}
