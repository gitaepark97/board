package board.backend.comment.domain;

import board.backend.common.exception.Forbidden;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record Comment(
    Long id,
    Long articleId,
    Long writerId,
    Long parentId,
    String content,
    LocalDateTime createdAt,
    Boolean isDeleted
) {

    public static Comment create(
        Long id,
        Long articleId,
        Long writerId,
        Long parentId,
        String content,
        LocalDateTime now
    ) {
        return Comment.builder()
            .id(id)
            .articleId(articleId)
            .writerId(writerId)
            .parentId(parentId == null ? id : parentId)
            .content(content)
            .createdAt(now)
            .isDeleted(false)
            .build();
    }

    @JsonIgnore
    public boolean isRoot() {
        return parentId.longValue() == id.longValue();
    }

    public Comment delete() {
        return toBuilder()
            .isDeleted(true)
            .build();
    }

    public void checkIsWriter(Long userId) {
        if (userId.longValue() != writerId.longValue()) {
            throw new Forbidden();
        }
    }

}
