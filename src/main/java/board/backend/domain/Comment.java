package board.backend.domain;

import board.backend.support.Forbidden;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    private Long id;

    private Long articleId;

    private Long writerId;

    private Long parentId;

    private String content;

    private LocalDateTime createdAt;

    private Boolean isDeleted;

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

    public boolean isRoot() {
        return parentId.longValue() == id.longValue();
    }

    public Comment delete() {
        this.isDeleted = true;
        return this;
    }

    public void checkIsWriter(Long userId) {
        if (userId.longValue() != writerId.longValue()) {
            throw new Forbidden();
        }
    }

}
