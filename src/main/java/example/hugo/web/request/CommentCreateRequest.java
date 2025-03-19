package example.hugo.web.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(
    @NotNull(message = "게시글 ID는 필수값입니다.")
    Long articleId,

    @NotBlank(message = "내용은 필수값입니다.")
    String content,

    Long parentCommentId,

    @NotNull(message = "작성자 ID는 필수값입니다.")
    Long writerId
) {

}
