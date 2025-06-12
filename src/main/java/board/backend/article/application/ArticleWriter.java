package board.backend.article.application;

import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.article.infra.jpa.ArticleRepository;
import board.backend.board.application.BoardReader;
import board.backend.common.event.ArticleCreatedEvent;
import board.backend.common.event.ArticleDeletedEvent;
import board.backend.common.infra.CacheRepository;
import board.backend.common.support.IdProvider;
import board.backend.common.support.TimeProvider;
import board.backend.user.application.UserReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleWriter {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final CacheRepository<Article, Long> articleCacheRepository;
    private final ArticleRepository articleRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final BoardReader boardReader;
    private final UserReader userReader;

    @Transactional
    public Article create(Long boardId, Long userId, String title, String content) {
        // 게시판 존재 확인
        boardReader.checkBoardExistsOrThrow(boardId);

        // 회원 존재 확인
        userReader.checkUserExistsOrThrow(userId);

        // 게시글 생성
        Article newArticle = Article.create(idProvider.nextId(), boardId, userId, title, content, timeProvider.now());
        // 게시글 저장
        articleRepository.save(newArticle);

        // 게시글 생 이벤트 발행
        eventPublisher.publishEvent(new ArticleCreatedEvent(newArticle.getId()));

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
            // 게시글 캐시 삭제
            articleCacheRepository.delete(articleId);

            // 게시글 삭제 이벤트 발행
            eventPublisher.publishEvent(new ArticleDeletedEvent(articleId));
        }));
    }

}
