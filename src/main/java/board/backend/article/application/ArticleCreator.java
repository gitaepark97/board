package board.backend.article.application;

import board.backend.article.application.port.ArticleRepository;
import board.backend.article.domain.Article;
import board.backend.board.application.BoardValidator;
import board.backend.common.event.EventPublisher;
import board.backend.common.event.EventType;
import board.backend.common.event.payload.ArticleCreatedEventPayload;
import board.backend.common.support.IdProvider;
import board.backend.common.support.TimeProvider;
import board.backend.user.application.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleCreator {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final ArticleRepository articleRepository;
    private final EventPublisher eventPublisher;
    private final UserValidator userValidator;
    private final BoardValidator boardValidator;

    @Transactional
    Article create(Long boardId, Long userId, String title, String content) {
        // 게시판 존재 확인
        boardValidator.checkBoardExistsOrThrow(boardId);

        // 사용자 존재 확인
        userValidator.checkUserExistsOrThrow(userId);

        // 게시글 생성
        Article newArticle = Article.create(idProvider.nextId(), boardId, userId, title, content, timeProvider.now());
        // 게시글 저장
        articleRepository.save(newArticle);

        // 게시글 생성 이벤트 발행
        eventPublisher.publishEvent(EventType.ARTICLE_CREATED, new ArticleCreatedEventPayload(newArticle.id()));

        return newArticle;
    }

}
