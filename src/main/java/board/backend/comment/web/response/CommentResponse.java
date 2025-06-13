package board.backend.comment.web.response;

import board.backend.comment.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
    String id,
    String writerId,
    String parentId,
    String content,
    LocalDateTime createdAt,
    Boolean isDeleted
) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
            comment.id().toString(),
            comment.writerId().toString(),
            comment.parentId().toString(),
            comment.content(),
            comment.createdAt(),
            comment.isDeleted()
        );
    }

}
