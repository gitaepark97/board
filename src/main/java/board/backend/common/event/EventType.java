package board.backend.common.event;

import board.backend.common.event.payload.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@RequiredArgsConstructor
@Slf4j
public enum EventType {

    ARTICLE_READ(ArticleReadEventPayload.class),
    ARTICLE_DELETED(ArticleDeletedEventPayload.class),
    ARTICLE_COMMENT_COUNT_CHANGED(ArticleCommentCountChangedEventPayload.class),
    ARTICLE_LIKE_COUNT_CHANGED(ArticleLikeCountChangedEventPayload.class),
    ARTICLE_VIEW_COUNT_CHANGED(ArticleViewCountChangedEventPayload.class);

    private final Class<? extends EventPayload> payloadClass;

    public static EventType from(String type) {
        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("[EventType.from] type={}", type, e);
            return null;
        }
    }

}
