package board.backend.comment.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(
    @NotNull
    Long articleId,

    Long parentCommentId,

    @NotBlank
    String content
) {

}
