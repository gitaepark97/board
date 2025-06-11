package board.backend.board.application;

import board.backend.board.domain.Board;
import board.backend.board.domain.BoardNotFound;
import board.backend.board.infra.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BoardReaderTest {

    private BoardRepository boardRepository;
    private BoardReader boardReader;

    @BeforeEach
    void setUp() {
        boardRepository = mock(BoardRepository.class);
        boardReader = new BoardReader(boardRepository);
    }

    @Test
    @DisplayName("게시판이 존재하면 예외 없이 통과한다")
    void checkBoardExistsOrThrow_whenExists_shouldPass() {
        // given
        Long boardId = 1L;
        when(boardRepository.customExistsById(boardId)).thenReturn(true);

        // when
        boardReader.checkBoardExistsOrThrow(boardId);
    }

    @Test
    @DisplayName("게시판이 존재하지 않으면 예외가 발생한다")
    void checkBoardExistsOrThrow_whenNotExists_shouldThrow() {
        // given
        Long boardId = 1L;
        when(boardRepository.customExistsById(boardId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> boardReader.checkBoardExistsOrThrow(boardId))
            .isInstanceOf(BoardNotFound.class);
    }

    @Test
    @DisplayName("모든 게시판 목록을 조회한다")
    void readAll_shouldReturnAllBoards() {
        // given
        List<Board> boards = List.of(Board.builder().id(1L).title("게시판1").createdAt(LocalDateTime.now()).build());
        when(boardRepository.findAll()).thenReturn(boards);

        // when
        List<Board> result = boardReader.readAll();

        // then
        assertThat(result).isEqualTo(boards);
    }

}