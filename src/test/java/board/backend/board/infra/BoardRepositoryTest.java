package board.backend.board.infra;

import board.backend.board.domain.Board;
import board.backend.common.infra.TestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Import(CustomBoardRepositoryImpl.class)
class BoardRepositoryTest extends TestRepository {

    private final Long boardId = 1L;
    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void setUp() {
        Board board = Board.builder().id(boardId).title("게시판1").createdAt(LocalDateTime.now()).build();
        boardRepository.save(board);
    }

    @Test
    @DisplayName("ID로 게시판 존재 여부를 확인한다 - 존재함")
    void customExistsById_exists() {
        // when
        boolean result = boardRepository.customExistsById(boardId);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("ID로 게시판 존재 여부를 확인한다 - 존재하지 않음")
    void customExistsById_notExists() {
        // when
        boolean result = boardRepository.customExistsById(999L);

        // then
        assertThat(result).isFalse();
    }

}