package board.backend.web;

import board.backend.application.CommentService;
import board.backend.domain.Comment;
import board.backend.web.request.CommentCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest extends TestController {

    @MockitoBean
    private CommentService commentService;

    @Test
    @DisplayName("댓글 목록 조회 API - 성공")
    void readAll_success() throws Exception {
        // given
        Long articleId = 1L;
        Long pageSize = 10L;
        Long lastParentCommentId = 5L;
        Long lastCommentId = 7L;

        List<Comment> comments = List.of(
            Comment.create(8L, articleId, 100L, 8L, "댓글1", LocalDateTime.of(2024, 1, 1, 10, 0)),
            Comment.create(9L, articleId, 101L, 9L, "댓글2", LocalDateTime.of(2024, 1, 1, 11, 0))
        );

        when(commentService.readAll(articleId, pageSize, lastParentCommentId, lastCommentId))
            .thenReturn(comments);

        // when & then
        mockMvc.perform(get("/api/comments")
                .param("articleId", articleId.toString())
                .param("pageSize", pageSize.toString())
                .param("lastParentCommentId", lastParentCommentId.toString())
                .param("lastCommentId", lastCommentId.toString())
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data[0].id").value(8L))
            .andExpect(jsonPath("$.data[0].content").value("댓글1"))
            .andExpect(jsonPath("$.data[1].id").value(9L))
            .andExpect(jsonPath("$.data[1].content").value("댓글2"))
            .andDo(document("comments/read-all",
                queryParameters(
                    parameterWithName("articleId").description("게시글 ID"),
                    parameterWithName("pageSize").optional().description("가져올 댓글 수 (기본값: 10)"),
                    parameterWithName("lastParentCommentId").optional().description("마지막 부모 댓글 ID"),
                    parameterWithName("lastCommentId").optional().description("마지막 댓글 ID")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data[].id").description("댓글 ID"),
                    fieldWithPath("data[].writerId").description("작성자 ID"),
                    fieldWithPath("data[].parentId").description("부모 댓글 ID"),
                    fieldWithPath("data[].content").description("댓글 내용"),
                    fieldWithPath("data[].createdAt").description("작성 시각"),
                    fieldWithPath("data[].isDeleted").description("삭제 여부")
                )
            ));
    }

    @Test
    @DisplayName("댓글 생성 API - 성공")
    void create_success() throws Exception {
        // given
        Long userId = 1L;
        String accessToken = "valid-access-token";
        CommentCreateRequest request = new CommentCreateRequest(
            1L,
            100L,
            "댓글 내용"
        );

        Comment comment = Comment.create(
            10L,
            1L,
            userId,
            100L,
            "댓글 내용",
            LocalDateTime.of(2024, 1, 1, 12, 0)
        );

        when(commentService.create(anyLong(), anyLong(), anyLong(), anyString())).thenReturn(comment);

        // when & then
        mockMvc.perform(post("/api/comments")
                .with(authentication(new UsernamePasswordAuthenticationToken(userId, null, null)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("CREATED"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.id").value(10L))
            .andExpect(jsonPath("$.data.content").value("댓글 내용"))
            .andDo(document("comments/create",
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token: Bearer 타입")
                ),
                requestFields(
                    fieldWithPath("articleId").description("댓글을 작성할 게시글 ID"),
                    fieldWithPath("parentCommentId").optional().description("부모 댓글 ID"),
                    fieldWithPath("content").description("댓글 내용")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data.id").description("댓글 ID"),
                    fieldWithPath("data.writerId").description("작성자 ID"),
                    fieldWithPath("data.parentId").description("부모 댓글 ID"),
                    fieldWithPath("data.content").description("댓글 내용"),
                    fieldWithPath("data.createdAt").description("작성 시각"),
                    fieldWithPath("data.isDeleted").description("삭제 여부")
                )
            ));
    }

    @Test
    @DisplayName("댓글 삭제 API - 성공")
    void delete_success() throws Exception {
        // given
        Long userId = 1L;
        String accessToken = "valid-access-token";
        Long commentId = 10L;

        doNothing().when(commentService).delete(commentId, userId);

        // when & then
        mockMvc.perform(delete("/api/comments/{commentId}", commentId)
                .with(authentication(new UsernamePasswordAuthenticationToken(userId, null, null)))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andDo(document("comments/delete",
                requestHeaders(
                    headerWithName(HttpHeaders.AUTHORIZATION).description("Access Token: Bearer 타입")
                ),
                pathParameters(
                    parameterWithName("commentId").description("삭제할 댓글 ID")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지")
                )
            ));
    }

}