package board.backend.web;

import board.backend.application.AuthService;
import board.backend.application.dto.Token;
import board.backend.domain.Session;
import board.backend.web.request.LoginRequest;
import board.backend.web.request.RegisterRequest;
import board.backend.web.response.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
class AuthController {

    private static final String REFRESH_TOKEN_KEY = "refreshToken";

    private final AuthService authService;

    @PostMapping("/register")
    void register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request.email(), request.password(), request.nickname());
    }

    @PostMapping("/login")
    ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        Token token = authService.login(request.email(), request.password());

        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.SET_COOKIE, setRefreshTokenCookie(token.refresh()))
            .body(TokenResponse.from(token));
    }

    private String setRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_KEY, refreshToken)
            .httpOnly(true)
            .secure(true)
            .sameSite("None")
            .path("/")
            .maxAge(Session.SESSION_DURATION.toMillis())
            .build()
            .toString();
    }

}
