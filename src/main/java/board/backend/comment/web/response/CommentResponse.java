package board.backend.comment.web.response;

import board.backend.comment.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
    Long id,
    Long writerId,
    Long parentId,
    String content,
    LocalDateTime createdAt,
    Boolean isDeleted
) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
            comment.getId(),
            comment.getWriterId(),
            comment.getParentId(),
            comment.getContent(),
            comment.getCreatedAt(),
            comment.getIsDeleted()
        );
    }

}
