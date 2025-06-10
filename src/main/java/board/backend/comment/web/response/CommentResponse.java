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
            comment.getId().toString(),
            comment.getWriterId().toString(),
            comment.getParentId().toString(),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getIsDeleted()
        );
    }

}
