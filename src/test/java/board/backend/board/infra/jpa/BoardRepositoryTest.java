package board.backend.board.infra.jpa;

import board.backend.board.application.port.BoardRepository;
import board.backend.board.domain.Board;
import board.backend.common.config.TestJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Import(BoardRepositoryImpl.class)
class BoardRepositoryTest extends TestJpaRepository {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardEntityRepository boardEntityRepository;

    @Test
    @DisplayName("게시판이 존재하면 true를 반환한다")
    void existsById_success_whenExists_returnsTrue() {
        // given
        Board board = new Board(1L, "게시판1", LocalDateTime.now());
        boardEntityRepository.save(BoardEntity.from(board));

        // when
        boolean result = boardRepository.existsById(board.id());

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("게시판이 존재하지 않으면 false를 반환한다")
    void existsById_success_whenNotExists_returnsFalse() {
        // when
        boolean result = boardRepository.existsById(999L);

        // then
        assertThat(result).isFalse();
    }

}