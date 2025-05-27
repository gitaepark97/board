package board.backend.controller;

import board.backend.service.ArticleLikeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleLikeController.class)
class ArticleLikeControllerTest extends TestController {

    @MockitoBean
    private ArticleLikeService articleLikeService;


    @Test
    @DisplayName("게시글 좋아요 API - 성공")
    void like_success() throws Exception {
        // given
        Long articleId = 1L;
        Long userId = 100L;

        doNothing().when(articleLikeService).like(articleId, userId);

        // when & then
        mockMvc.perform(post("/api/article-likes/articles/{articleId}/users/{userId}", articleId, userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
            .andExpect(jsonPath("$.message").value("성공"))
            .andDo(document("article-likes/like",
                pathParameters(
                    parameterWithName("articleId").description("좋아요할 게시글 ID"),
                    parameterWithName("userId").description("좋아요를 누른 사용자 ID")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지")
                )
            ));
    }

    @Test
    @DisplayName("게시글 좋아요 취소 API - 성공")
    void unlike_success() throws Exception {
        // given
        Long articleId = 1L;
        Long userId = 100L;

        doNothing().when(articleLikeService).unlike(articleId, userId);

        // when & then
        mockMvc.perform(delete("/api/article-likes/articles/{articleId}/users/{userId}", articleId, userId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value(HttpStatus.OK.name()))
            .andExpect(jsonPath("$.message").value("성공"))
            .andDo(document("article-likes/unlike",
                pathParameters(
                    parameterWithName("articleId").description("좋아요를 취소할 게시글 ID"),
                    parameterWithName("userId").description("좋아요를 취소하는 사용자 ID")
                ),
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지")
                )
            ));
    }

}