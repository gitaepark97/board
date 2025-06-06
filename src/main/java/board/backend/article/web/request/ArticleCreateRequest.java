package board.backend.article.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArticleCreateRequest(
    @NotNull
    Long boardId,

    @NotBlank
    String title,

    @NotBlank
    String content
) {

}
