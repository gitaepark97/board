package board.backend.user.web;

import board.backend.common.web.TestController;
import board.backend.user.application.UserService;
import board.backend.user.domain.User;
import board.backend.user.web.request.UserUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest extends TestController {

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("내 정보 조회 API - 성공")
    void readMy_success() throws Exception {
        // given
        Long userId = 1L;
        String accessToken = "valid-access-token";
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);
        User user = User.create(userId, "user@example.com", "닉네임", now);
        when(userService.read(userId)).thenReturn(user);

        // when & then
        mockMvc.perform(get("/api/users/my")
                .with(authentication(new UsernamePasswordAuthenticationToken(userId, null, null)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.id").value(userId))
            .andExpect(jsonPath("$.data.email").value("user@example.com"))
            .andExpect(jsonPath("$.data.nickname").value("닉네임"))
            .andDo(document("users/read-my",
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token: Bearer 타입")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.id").type(JsonFieldType.STRING).description("사용자 ID"),
                    fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                    fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("가입 시각"),
                    fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).description("수정 시각")
                )
            ));
    }

    @Test
    @DisplayName("사용자 조회 API - 성공")
    void read_success() throws Exception {
        // given
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);
        User user = User.create(userId, "user@example.com", "닉네임", now);
        when(userService.read(userId)).thenReturn(user);

        // when & then
        mockMvc.perform(get("/api/users/{userId}", userId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.id").value(userId.toString()))
            .andExpect(jsonPath("$.data.nickname").value("닉네임"))
            .andDo(document("users/read",
                pathParameters(
                    parameterWithName("userId").description("조회할 사용자 ID")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.id").type(JsonFieldType.STRING).description("사용자 ID"),
                    fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("닉네임")
                )
            ));
    }

    @Test
    @DisplayName("내 정보 수정 API - 성공")
    void update_success() throws Exception {
        // given
        Long userId = 1L;
        String accessToken = "valid-access-token";
        UserUpdateRequest request = new UserUpdateRequest("새닉네임");
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);
        User user = User.create(userId, "user@example.com", "새닉네임", now);
        when(userService.update(userId, "새닉네임")).thenReturn(user);

        // when & then
        mockMvc.perform(put("/api/users/my")
                .with(authentication(new UsernamePasswordAuthenticationToken(userId, null, null)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.id").value(userId))
            .andExpect(jsonPath("$.data.email").value("user@example.com"))
            .andExpect(jsonPath("$.data.nickname").value("새닉네임"))
            .andDo(document("users/update-my",
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token: Bearer 타입")
                ),
                requestFields(
                    fieldWithPath("nickname").type(JsonFieldType.STRING).description("수정할 닉네임")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.id").type(JsonFieldType.STRING).description("사용자 ID"),
                    fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                    fieldWithPath("data.nickname").type(JsonFieldType.STRING).description("닉네임"),
                    fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("가입 시각"),
                    fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).description("수정 시각")
                )
            ));
    }

}