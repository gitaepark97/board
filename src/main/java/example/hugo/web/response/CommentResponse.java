package example.hugo.web.response;

import example.hugo.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
    Long commentId,
    String content,
    Long parentCommentId,
    Long articleId,
    Long writerId,
    Boolean isDeleted,
    LocalDateTime createdAt
) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
            comment.commentId(),
            comment.content(),
            comment.parentCommentId(),
            comment.articleId(),
            comment.writerId(),
            comment.isDeleted(),
            comment.createdAt()
        );
    }

}
