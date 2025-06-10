package board.backend.article.web.request;

import board.backend.common.web.request.NumericString;
import jakarta.validation.constraints.NotBlank;

public record ArticleCreateRequest(
    @NotBlank
    @NumericString
    String boardId,

    @NotBlank
    String title,

    @NotBlank
    String content
) {

    public Long boardIdAsLong() {
        return Long.valueOf(boardId);
    }

}
