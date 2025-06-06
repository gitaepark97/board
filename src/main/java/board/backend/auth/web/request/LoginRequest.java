package board.backend.auth.web.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank
    String email,

    @NotBlank
    String password
) {

}
