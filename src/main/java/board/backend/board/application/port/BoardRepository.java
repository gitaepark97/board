package board.backend.board.application.port;

import board.backend.board.domain.Board;

import java.util.List;

public interface BoardRepository {

    boolean existsById(Long id);

    List<Board> findAll();

}
