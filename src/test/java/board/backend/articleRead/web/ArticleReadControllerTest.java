package board.backend.articleRead.web;

import board.backend.article.domain.Article;
import board.backend.articleRead.application.ArticleReadService;
import board.backend.articleRead.application.dto.ArticleWithWriterAndCounts;
import board.backend.common.web.TestController;
import board.backend.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleReadController.class)
class ArticleReadControllerTest extends TestController {

    @MockitoBean
    private ArticleReadService articleReadService;

    @Test
    @DisplayName("게시글 목록 조회 API - 성공")
    void readAll_success() throws Exception {
        // given
        Long boardId = 1L;
        Long pageSize = 10L;
        Long lastArticleId = 5L;

        LocalDateTime now = LocalDateTime.of(2024, 1, 1, 10, 0);

        List<ArticleWithWriterAndCounts> responses = List.of(
            ArticleWithWriterAndCounts.of(Article.create(6L, boardId, 1L, "제목1", "내용1", now), User.create(1L, "user1@email.com", "회원1", LocalDateTime.now()), 1L, 1L, 1L),
            ArticleWithWriterAndCounts.of(Article.create(7L, boardId, 2L, "제목2", "내용2", now), User.create(2L, "user2@email.com", "회원2", LocalDateTime.now()), 0L, 0L, 0L)
        );

        when(articleReadService.readAll(boardId, pageSize, lastArticleId)).thenReturn(responses);

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
            .andExpect(jsonPath("$.data[0].id").value("6"))
            .andExpect(jsonPath("$.data[0].title").value("제목1"))
            .andExpect(jsonPath("$.data[0].writer.id").value("1"))
            .andExpect(jsonPath("$.data[0].writer.nickname").value("회원1"))
            .andExpect(jsonPath("$.data[0].likeCount").value(1L))
            .andExpect(jsonPath("$.data[0].viewCount").value(1L))
            .andExpect(jsonPath("$.data[0].commentCount").value(1L))
            .andExpect(jsonPath("$.data[1].id").value("7"))
            .andExpect(jsonPath("$.data[1].writer.id").value("2"))
            .andExpect(jsonPath("$.data[1].writer.nickname").value("회원2"))
            .andExpect(jsonPath("$.data[1].title").value("제목2"))
            .andExpect(jsonPath("$.data[1].likeCount").value(0L))
            .andExpect(jsonPath("$.data[1].viewCount").value(0L))
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
                    fieldWithPath("data[].id").type(JsonFieldType.STRING).description("게시글 ID"),
                    fieldWithPath("data[].boardId").type(JsonFieldType.STRING).description("게시판 ID"),
                    fieldWithPath("data[].writer.id").type(JsonFieldType.STRING).description("작성자 ID"),
                    fieldWithPath("data[].writer.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                    fieldWithPath("data[].title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("생성 시각"),
                    fieldWithPath("data[].likeCount").type(JsonFieldType.NUMBER).description("좋아요 수"),
                    fieldWithPath("data[].viewCount").type(JsonFieldType.NUMBER).description("조회 수"),
                    fieldWithPath("data[].commentCount").type(JsonFieldType.NUMBER).description("댓글 수")
                )
            ));
    }

}