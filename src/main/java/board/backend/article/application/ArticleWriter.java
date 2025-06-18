package board.backend.article.application;

import board.backend.article.application.port.ArticleRepository;
import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.board.application.BoardReader;
import board.backend.common.event.EventPublisher;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleCreatedEventPayload;
import board.backend.common.event.payload.ArticleDeletedEventPayload;
import board.backend.common.infra.CachedRepository;
import board.backend.common.support.IdProvider;
import board.backend.common.support.TimeProvider;
import board.backend.user.application.UserReader;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
class ArticleWriter {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final CachedRepository<Article, Long> articleCachedRepository;
    private final ArticleRepository articleRepository;
    private final BoardReader boardReader;
    private final UserReader userReader;
    private final EventPublisher eventPublisher;

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

        // 게시글 생성 이벤트 발행
        eventPublisher.publishEvent(EventType.ARTICLE_CREATED, new ArticleCreatedEventPayload(newArticle.id()));

        return newArticle;
    }

    @Transactional
    public Article update(Long articleId, Long userId, String title, String content) {
        // 게시글 조회
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFound::new);
        // 게시글 캐시 삭제
        articleCachedRepository.delete(articleId);

        // 게시글 수정
        Article updatedArticle = article.update(userId, title, content, timeProvider.now());
        // 게시글 저장
        articleRepository.save(updatedArticle);

        return updatedArticle;
    }

    @Transactional
    public Optional<Long> delete(Long articleId, Long userId) {
        return articleRepository.findById(articleId).map(article -> {
            // 작성자 확인
            article.checkIsWriter(userId);
            // 게시글 삭제
            articleRepository.delete(article);
            // 게시글 캐시 삭제
            articleCachedRepository.delete(articleId);

            // 게시글 삭제 이벤트 발행
            eventPublisher.publishEvent(EventType.ARTICLE_DELETED, new ArticleDeletedEventPayload(articleId));

            return article.boardId();
        });
    }

}
