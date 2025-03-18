package example.hugo.web.request;

import jakarta.validation.constraints.NotBlank;

public record ArticleUpdateRequest(
    @NotBlank(message = "제목은 필수값입니다.")
    String title,

    @NotBlank(message = "내용은 필수값입니다.")
    String content
) {

}
