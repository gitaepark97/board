package board.backend.web;

import board.backend.application.ArticleService;
import board.backend.application.dto.ArticleWithCounts;
import board.backend.domain.Article;
import board.backend.web.request.ArticleCreateRequest;
import board.backend.web.request.ArticleUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest extends TestController {

    @MockitoBean
    private ArticleService articleService;

    @Test
    @DisplayName("게시글 목록 조회 API - 성공")
    void readAll_success() throws Exception {
        // given
        Long boardId = 1L;
        Long pageSize = 10L;
        Long lastArticleId = 5L;

        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);

        List<ArticleWithCounts> responses = List.of(
            ArticleWithCounts.of(Article.create(6L, boardId, 100L, "제목1", "내용1", now), 1L, 1L),
            ArticleWithCounts.of(Article.create(7L, boardId, 101L, "제목2", "내용2", now), 0L, 0L)
        );

        when(articleService.readAll(boardId, pageSize, lastArticleId)).thenReturn(responses);

        // when & then
        mockMvc.perform(get("/api/articles")
                .param("boardId", boardId.toString())
                .param("pageSize", pageSize.toString())
                .param("lastArticleId", lastArticleId.toString())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.length()").value(responses.size()))
            .andExpect(jsonPath("$.data[0].id").value(6L))
            .andExpect(jsonPath("$.data[0].title").value("제목1"))
            .andExpect(jsonPath("$.data[0].likeCount").value(1L))
            .andExpect(jsonPath("$.data[0].commentCount").value(1L))
            .andExpect(jsonPath("$.data[1].id").value(7L))
            .andExpect(jsonPath("$.data[1].title").value("제목2"))
            .andExpect(jsonPath("$.data[1].likeCount").value(0L))
            .andExpect(jsonPath("$.data[1].commentCount").value(0L))
            .andDo(document("articles/read-all",
                queryParameters(
                    parameterWithName("boardId").description("게시판 ID"),
                    parameterWithName("pageSize").optional().description("페이지 크기 (기본값: 10)"),
                    parameterWithName("lastArticleId").optional().description("마지막으로 조회한 게시글 ID (커서 기반 페이징)")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data[].id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                    fieldWithPath("data[].boardId").type(JsonFieldType.NUMBER).description("게시판 ID"),
                    fieldWithPath("data[].writerId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                    fieldWithPath("data[].title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("생성 시각"),
                    fieldWithPath("data[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                    fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER).description("댓글 수")
                )
            ));
    }

    @Test
    @DisplayName("게시글 조회 API - 성공")
    void read_success() throws Exception {
        // given
        Long articleId = 1L;
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);
        Article response = Article.create(articleId, 1L, 100L, "조회 제목", "조회 내용", now);

        when(articleService.read(articleId)).thenReturn(response);

        // when & then
        mockMvc.perform(get("/api/articles/{articleId}", articleId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.id").value(articleId))
            .andExpect(jsonPath("$.data.title").value("조회 제목"))
            .andDo(document("articles/read",
                pathParameters(
                    parameterWithName("articleId").description("조회할 게시글 ID")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                    fieldWithPath("data.boardId").type(JsonFieldType.NUMBER).description("게시판 ID"),
                    fieldWithPath("data.writerId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                    fieldWithPath("data.title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("data.content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("data.createdAt").type(JsonFieldType.STRING).description("생성 시각"),
                    fieldWithPath("data.updatedAt").type(JsonFieldType.STRING).description("수정 시각")
                )
            ));
    }

    @Test
    @DisplayName("게시글 생성 API - 성공")
    void create_success() throws Exception {
        // given
        ArticleCreateRequest request = new ArticleCreateRequest(1L, 100L, "제목입니다", "내용입니다");
        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);
        Article response = Article.create(1L, 1L, 100L, "제목입니다", "내용입니다", now);

        when(articleService.create(any(), any(), any(), any()))
            .thenReturn(response);

        // when & then
        mockMvc.perform(post("/api/articles")
                .with(authentication(new UsernamePasswordAuthenticationToken(1L, null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value(HttpStatus.CREATED.name()))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.id").value(1L))
            .andExpect(jsonPath("$.data.title").value("제목입니다"))
            .andDo(document("articles/create",
                requestFields(
                    fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 ID"),
                    fieldWithPath("writerId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                    fieldWithPath("data.boardId").type(JsonFieldType.NUMBER).description("게시판 ID"),
                    fieldWithPath("data.writerId").type(JsonFieldType.NUMBER).description("작성자 ID"),
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
        Long articleId = 1L;
        ArticleUpdateRequest request = new ArticleUpdateRequest("수정된 제목", "수정된 내용");

        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 2, 12, 0);
        Article response = Article.create(articleId, 1L, 100L, "수정된 제목", "수정된 내용", createdAt)
            .update("수정된 제목", "수정된 내용", updatedAt);

        when(articleService.update(eq(articleId), any(), any())).thenReturn(response);

        // when & then
        mockMvc.perform(put("/api/articles/{articleId}", articleId)
                .with(authentication(new UsernamePasswordAuthenticationToken(1L, null, null)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.id").value(articleId))
            .andExpect(jsonPath("$.data.title").value("수정된 제목"))
            .andDo(document("articles/update",
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
                    fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                    fieldWithPath("data.boardId").type(JsonFieldType.NUMBER).description("게시판 ID"),
                    fieldWithPath("data.writerId").type(JsonFieldType.NUMBER).description("작성자 ID"),
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
        Long articleId = 1L;

        // when & then
        mockMvc.perform(delete("/api/articles/{articleId}", articleId)
                .with(authentication(new UsernamePasswordAuthenticationToken(1L, null, null))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
            .andExpect(jsonPath("$.message").value("성공"))
            .andDo(document("articles/delete",
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