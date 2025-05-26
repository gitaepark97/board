package board.backend.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArticleCreateRequest(
    @NotNull
    Long boardId,

    @NotNull
    Long writerId,

    @NotBlank
    String title,

    @NotBlank
    String content
) {

}
