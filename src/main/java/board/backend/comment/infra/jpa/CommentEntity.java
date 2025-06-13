package board.backend.comment.infra.jpa;

import board.backend.comment.domain.Comment;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "comment")
class CommentEntity {

    @Id
    private Long id;

    private Long articleId;

    private Long writerId;

    private Long parentId;

    private String content;

    private LocalDateTime createdAt;

    private Boolean isDeleted;

    static CommentEntity from(Comment comment) {
        return new CommentEntity(
            comment.id(),
            comment.articleId(),
            comment.writerId(),
            comment.parentId(),
            comment.content(),
            comment.createdAt(),
            comment.isDeleted()
        );
    }

    Comment toComment() {
        return Comment.builder()
            .id(id)
            .articleId(articleId)
            .writerId(writerId)
            .parentId(parentId)
            .content(content)
            .createdAt(createdAt)
            .isDeleted(isDeleted)
            .build();
    }

}
