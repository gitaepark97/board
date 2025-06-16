package board.backend.comment.web.response;

import board.backend.comment.application.dto.CommentWithWriter;
import board.backend.user.web.response.UserResponse;

import java.time.LocalDateTime;

public record CommentWithWriterResponse(
    String id,
    String parentId,
    UserResponse writer,
    String content,
    LocalDateTime createdAt,
    Boolean isDeleted
) {

    public static CommentWithWriterResponse from(CommentWithWriter commentWithWriter) {
        return new CommentWithWriterResponse(
            commentWithWriter.comment().id().toString(),
            commentWithWriter.comment().parentId().toString(),
            UserResponse.from(commentWithWriter.writer()),
            commentWithWriter.comment().content(),
            commentWithWriter.comment().createdAt(),
            commentWithWriter.comment().isDeleted()
        );
    }

}
