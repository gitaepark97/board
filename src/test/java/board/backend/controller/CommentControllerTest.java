package board.backend.controller;

import board.backend.application.CommentService;
import board.backend.controller.request.CommentCreateRequest;
import board.backend.domain.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
class CommentControllerTest extends TestController {

    @MockitoBean
    private CommentService commentService;

    @DisplayName("댓글 생성 API - 성공")
    @Test
    void create_success() throws Exception {
        // given
        CommentCreateRequest request = new CommentCreateRequest(
            1L,
            100L,
            100L,
            "댓글 내용"
        );

        Comment comment = Comment.create(
            10L,
            1L,
            100L,
            100L,
            "댓글 내용",
            LocalDateTime.of(2024, 1, 1, 12, 0)
        );

        when(commentService.create(anyLong(), anyLong(), anyLong(), anyString())).thenReturn(comment);

        // when & then
        mockMvc.perform(post("/api/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value("CREATED"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data.id").value(10L))
            .andExpect(jsonPath("$.data.content").value("댓글 내용"))
            .andDo(document("comments/create",
                requestFields(
                    fieldWithPath("articleId").description("댓글을 작성할 게시글 ID"),
                    fieldWithPath("writerId").description("댓글 작성자 ID"),
                    fieldWithPath("parentCommentId").description("부모 댓글 ID"),
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

    @DisplayName("댓글 삭제 API - 성공")
    @Test
    void delete_success() throws Exception {
        // given
        Long commentId = 10L;

        doNothing().when(commentService).delete(commentId);

        // when & then
        mockMvc.perform(delete("/api/comments/{commentId}", commentId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andDo(document("comments/delete",
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