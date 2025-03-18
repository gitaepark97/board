package example.hugo.domain;

import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
public record Comment(
    Long commentId,
    String content,
    Long parentCommentId,
    Long articleId,
    Long writerId,
    Boolean isDeleted,
    LocalDateTime createdAt,
    LocalDateTime deletedAt
) {

    public static Comment create(
        Long commentId,
        String content,
        Long parentCommentId,
        Long articleId,
        Long writerId,
        LocalDateTime now
    ) {
        return Comment.builder()
            .commentId(commentId)
            .content(content)
            .parentCommentId(parentCommentId == null ? commentId : parentCommentId)
            .articleId(articleId)
            .writerId(writerId)
            .isDeleted(false)
            .createdAt(now)
            .build();
    }

    public Comment delete(LocalDateTime now) {
        return toBuilder()
            .isDeleted(true)
            .deletedAt(now)
            .build();
    }

    public boolean isRoot() {
        return parentCommentId.longValue() == commentId.longValue();
    }

}
