package example.hugo.infra.entity;

import example.hugo.domain.Comment;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "comment")
@DynamicUpdate
public class CommentEntity {

    @Id
    private Long commentId;
    private String content;
    private Long parentCommentId;
    private Long articleId;
    private Long writerId;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

    public static CommentEntity from(Comment comment) {
        return CommentEntity.builder()
            .commentId(comment.commentId())
            .content(comment.content())
            .parentCommentId(comment.parentCommentId())
            .articleId(comment.articleId())
            .writerId(comment.writerId())
            .isDeleted(comment.isDeleted())
            .createdAt(comment.createdAt())
            .deletedAt(comment.deletedAt())
            .build();
    }

    public Comment toComment() {
        return Comment.builder()
            .commentId(commentId)
            .content(content)
            .parentCommentId(parentCommentId)
            .articleId(articleId)
            .writerId(writerId)
            .isDeleted(isDeleted)
            .createdAt(createdAt)
            .deletedAt(deletedAt)
            .build();
    }

}
