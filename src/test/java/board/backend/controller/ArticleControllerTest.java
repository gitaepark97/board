package board.backend.controller;

import board.backend.controller.request.ArticleCreateRequest;
import board.backend.controller.request.ArticleUpdateRequest;
import board.backend.domain.Article;
import board.backend.service.ArticleService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
@ExtendWith(RestDocumentationExtension.class)
class ArticleControllerTest {

    protected final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    protected WebApplicationContext context;
    protected MockMvc mockMvc;
    @MockitoBean
    private ArticleService articleService;

    @BeforeEach
    void init(RestDocumentationContextProvider provider) {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(documentationConfiguration(provider))
            .build();
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
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("제목입니다"))
            .andDo(document("articles/create",
                requestFields(
                    fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 ID"),
                    fieldWithPath("writerId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                ),
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                    fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 ID"),
                    fieldWithPath("writerId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시각"),
                    fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("수정 시각")
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
            .andExpect(jsonPath("$.id").value(articleId))
            .andExpect(jsonPath("$.title").value("조회 제목"))
            .andDo(document("articles/read",
                pathParameters(
                    parameterWithName("articleId").description("조회할 게시글 ID")
                ),
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                    fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 ID"),
                    fieldWithPath("writerId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시각"),
                    fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("수정 시각")
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
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(articleId))
            .andExpect(jsonPath("$.title").value("수정된 제목"))
            .andDo(document("articles/update",
                pathParameters(
                    parameterWithName("articleId").description("수정할 게시글 ID")
                ),
                requestFields(
                    fieldWithPath("title").type(JsonFieldType.STRING).description("수정할 제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 내용")
                ),
                responseFields(
                    fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 ID"),
                    fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시판 ID"),
                    fieldWithPath("writerId").type(JsonFieldType.NUMBER).description("작성자 ID"),
                    fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                    fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                    fieldWithPath("createdAt").type(JsonFieldType.STRING).description("생성 시각"),
                    fieldWithPath("updatedAt").type(JsonFieldType.STRING).description("수정 시각")
                )
            ));
    }


}