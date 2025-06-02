package board.backend.web.response;

import board.backend.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
    Long id,
    Long writerId,
    Long parentId,
    String content,
    LocalDateTime createdAt,
    Boolean isDeleted
) {

    public static CommentResponse of(Comment comment) {
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
