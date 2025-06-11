package board.backend.board.web;

import board.backend.board.application.BoardService;
import board.backend.board.domain.Board;
import board.backend.common.web.TestController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
class BoardControllerTest extends TestController {

    @MockitoBean
    private BoardService boardService;

    @Test
    @DisplayName("게시판 목록 조회 API - 성공")
    void readAll_success() throws Exception {
        // given
        List<Board> boards = List.of(Board.builder().id(1L).title("게시판1").createdAt(LocalDateTime.now()).build());
        when(boardService.readAll()).thenReturn(boards);

        // when & then
        mockMvc.perform(get("/api/boards"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.message").value("성공"))
            .andExpect(jsonPath("$.data[0].id").value("1"))
            .andExpect(jsonPath("$.data[0].title").value("게시판1"))
            .andDo(document("boards/read-all",
                responseFields(
                    fieldWithPath("status").description("HTTP 상태"),
                    fieldWithPath("message").description("응답 메시지"),
                    fieldWithPath("data[].id").type(JsonFieldType.STRING).description("게시판 ID"),
                    fieldWithPath("data[].title").type(JsonFieldType.STRING).description("게시판명")
                )
            ));
    }

}