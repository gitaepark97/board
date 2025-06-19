package board.backend.board.application;

import board.backend.board.application.fake.FakeBoardRepository;
import board.backend.board.domain.Board;
import board.backend.board.domain.BoardNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BoardValidatorTest {

    private FakeBoardRepository boardRepository;
    private BoardValidator boardValidator;

    @BeforeEach
    void setUp() {
        boardRepository = new FakeBoardRepository();
        boardValidator = new BoardValidator(boardRepository);
    }

    @Test
    @DisplayName("게시판이 존재하면 예외가 발생하지 않는다")
    void checkBoardExistsOrThrow_success_whenBoardExists_doesNotThrow() {
        // given
        Board board = new Board(1L, "공지사항", LocalDateTime.now());
        boardRepository.save(board);

        // when & then
        assertThatCode(() -> boardValidator.checkBoardExistsOrThrow(1L))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게시판이 존재하지 않으면 예외가 발생한다")
    void checkBoardExistsOrThrow_fail_whenBoardNotExists_throwsBoardNotFound() {
        // given - 저장된 게시판 없음

        // when & then
        assertThatThrownBy(() -> boardValidator.checkBoardExistsOrThrow(999L))
            .isInstanceOf(BoardNotFound.class);
    }

}