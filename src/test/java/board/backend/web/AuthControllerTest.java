package board.backend.web;

import board.backend.application.AuthService;
import board.backend.application.dto.Token;
import board.backend.web.request.LoginRequest;
import board.backend.web.request.RegisterRequest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.cookies.CookieDocumentation.cookieWithName;
import static org.springframework.restdocs.cookies.CookieDocumentation.requestCookies;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest extends TestController {

    @MockitoBean
    private AuthService authService;

    @Test
    @DisplayName("회원가입 API - 성공")
    void register_success() throws Exception {
        // given
        RegisterRequest request = new RegisterRequest("user@example.com", "Password123!", "닉네임1");

        // when & then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andDo(document("auth/register",
                requestFields(
                    fieldWithPath("email").description("회원 이메일"),
                    fieldWithPath("password").description("비밀번호"),
                    fieldWithPath("nickname").description("닉네임")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지")
                )
            ));
    }

    @Test
    @DisplayName("로그인 API - 성공")
    void login_success() throws Exception {
        // given
        LoginRequest request = new LoginRequest("user@example.com", "password123");
        Token token = new Token("access-token-value", "refresh-token-value");

        when(authService.login(anyString(), anyString())).thenReturn(token);

        // when & then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(header().exists(HttpHeaders.SET_COOKIE))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.accessToken").value("access-token-value"))
            .andDo(document("auth/login",
                requestFields(
                    fieldWithPath("email").type(JsonFieldType.STRING).description("로그인 이메일"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("엑세스 토큰")
                )
            ));
    }

    @Test
    @DisplayName("로그아웃 API - 성공")
    void logout_success() throws Exception {
        // given
        Long userId = 1L;

        doNothing().when(authService).logout(userId);

        // when & then
        mockMvc.perform(post("/api/auth/logout")
                .with(authentication(new UsernamePasswordAuthenticationToken(userId, null, null)))
                .header("Authorization", "Bearer access token")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data").doesNotExist())
            .andDo(document("auth/logout",
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token: Bearer 타입")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지")
                )
            ));
    }

    @Test
    @DisplayName("토큰 재발급 API - 성공")
    void reissueToken_success() throws Exception {
        // given
        String refreshToken = "valid-refresh-token";
        Token token = new Token("new-access-token", "new-refresh-token");

        when(authService.reissueToken(refreshToken)).thenReturn(token);

        // when & then
        mockMvc.perform(post("/api/auth/reissue-token")
                .cookie(new Cookie("refreshToken", refreshToken))
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(header().exists(HttpHeaders.SET_COOKIE))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.accessToken").value("new-access-token"))
            .andDo(document("auth/reissue-token",
                requestCookies(
                    cookieWithName("refreshToken").description("리프레시 토큰 (HttpOnly 쿠키)")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("새 엑세스 토큰")
                )
            ));
    }

}