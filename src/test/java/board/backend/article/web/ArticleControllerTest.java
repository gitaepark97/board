package board.backend.article.web;

import board.backend.article.application.ArticleService;
import board.backend.article.domain.Article;
import board.backend.article.web.request.ArticleCreateRequest;
import board.backend.article.web.request.ArticleUpdateRequest;
import board.backend.common.web.TestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest extends TestController {

    @MockitoBean
    private ArticleService articleService;

    @Test
    @DisplayName("게시글 생성 API - 성공")
    void create_success() throws Exception {
        // given
        Long userId = 1L;
        String accessToken = "valid-access-token";
        ArticleCreateRequest request = new ArticleCreateRequest("1", "제목입니다", "내용입니다");
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);
        Article response = Article.create(1L, 1L, userId, "제목입니다", "내용입니다", now);
        when(articleService.create(any(), any(), any(), any())).thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/articles")
                .with(authentication(new UsernamePasswordAuthenticationToken(userId, null, null)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.name()))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.id").value("1"))
            .andExpect(jsonPath("$.data.title").value("제목입니다"))
            .andDo(document("articles/create",
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token: Bearer 타입")
                ),
                requestFields(
                    fieldWithPath("boardId").type(JsonFieldType.STRING).description("게시판 ID"),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.id").type(JsonFieldType.STRING).description("게시글 ID"),
                    fieldWithPath("data.boardId").type(JsonFieldType.STRING).description("게시판 ID"),
                    fieldWithPath("data.writerId").type(JsonFieldType.STRING).description("작성자 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("생성 시각"),
                    fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).description("수정 시각")
                )
            ));
    }

    @Test
    @DisplayName("게시글 수정 API - 성공")
    void update_success() throws Exception {
        // given
        Long userId = 1L;
        String accessToken = "valid-access-token";
        Long articleId = 1L;
        ArticleUpdateRequest request = new ArticleUpdateRequest("수정된 제목", "수정된 내용");
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 2, 12, 0);
        Article response = Article.create(articleId, 1L, userId, "수정된 제목", "수정된 내용", createdAt)
            .update(userId, "수정된 제목", "수정된 내용", updatedAt);
        when(articleService.update(eq(userId), eq(articleId), any(), any())).thenReturn(response);

        // when & then
        mockMvc.perform(put("/api/articles/{articleId}", articleId)
                .with(authentication(new UsernamePasswordAuthenticationToken(userId, null, null)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.id").value(articleId.toString()))
            .andExpect(jsonPath("$.data.title").value("수정된 제목"))
            .andDo(document("articles/update",
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token: Bearer 타입")
                ),
                pathParameters(
                    parameterWithName("articleId").description("수정할 게시글 ID")
                ),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("수정할 제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 내용")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.id").type(JsonFieldType.STRING).description("게시글 ID"),
                    fieldWithPath("data.boardId").type(JsonFieldType.STRING).description("게시판 ID"),
                    fieldWithPath("data.writerId").type(JsonFieldType.STRING).description("작성자 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("생성 시각"),
                    fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).description("수정 시각")
                )
            ));
    }

    @Test
    @DisplayName("게시글 삭제 API - 성공")
    void delete_success() throws Exception {
        // given
        Long userId = 1L;
        String accessToken = "valid-access-token";
        Long articleId = 1L;

        // when & then
        mockMvc.perform(delete("/api/articles/{articleId}", articleId)
                .with(authentication(new UsernamePasswordAuthenticationToken(userId, null, null)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
            .andExpect(jsonPath("$.message").value("성공"))
            .andDo(document("articles/delete",
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token: Bearer 타입")
                ),
                pathParameters(
                    parameterWithName("articleId").description("삭제할 게시글 ID")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지")
                )
            ));
    }

}