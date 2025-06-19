package board.backend.board.application;

import board.backend.board.application.fake.FakeBoardRepository;
import board.backend.board.domain.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardReaderTest {

    private FakeBoardRepository boardRepository;
    private BoardReader boardReader;

    @BeforeEach
    void setUp() {
        boardRepository = new FakeBoardRepository();
        boardReader = new BoardReader(boardRepository);
    }

    @Test
    @DisplayName("모든 게시판 목록을 조회할 수 있다")
    void readAll_success_whenBoardsExist_returnsAllBoards() {
        // given
        Board board1 = new Board(1L, "공지사항", LocalDateTime.now());
        Board board2 = new Board(2L, "자유게시판", LocalDateTime.now());
        boardRepository.save(board1);
        boardRepository.save(board2);

        // when
        List<Board> result = boardReader.readAll();

        // then
        assertThat(result).containsExactlyInAnyOrder(board1, board2);
    }

    @Test
    @DisplayName("게시판이 없을 경우 빈 리스트를 반환한다")
    void readAll_success_whenNoBoards_returnsEmptyList() {
        // given - 저장된 게시판 없음

        // when
        List<Board> result = boardReader.readAll();

        // then
        assertThat(result).isEmpty();
    }

}