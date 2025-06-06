package board.backend.comment.web.response;

import board.backend.auth.application.dto.CommentWithWriter;
import board.backend.user.web.response.UserSummaryResponse;

import java.time.LocalDateTime;

public record CommentWithWriterResponse(
    Long id,
    Long parentId,
    UserSummaryResponse writer,
    String content,
    LocalDateTime createdAt,
    Boolean isDeleted
) {

    public static CommentWithWriterResponse from(CommentWithWriter commentWithWriter) {
        return new CommentWithWriterResponse(
            commentWithWriter.comment().getId(),
            commentWithWriter.comment().getParentId(),
            UserSummaryResponse.from(commentWithWriter.writer()),
            commentWithWriter.comment().getContent(),
            commentWithWriter.comment().getCreatedAt(),
            commentWithWriter.comment().getIsDeleted()
        );
    }

}
