package board.backend.article.web.request;

import jakarta.validation.constraints.NotBlank;

public record ArticleUpdateRequest(
    @NotBlank
    String title,

    @NotBlank
    String content
) {

}
