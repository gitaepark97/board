package example.hugo.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ArticleCreateRequest(
    @NotBlank(message = "제목은 필수값입니다.")
    String title,

    @NotBlank(message = "내용은 필수값입니다.")
    String content,

    @NotNull(message = "게시판 ID는 필수값입니다.")
    Long boardId,

    @NotNull(message = "작성자 ID는 필수값입니다.")
    Long writerId
) {

}
