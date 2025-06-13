package board.backend.board.application;

import board.backend.board.application.port.BoardRepository;
import board.backend.board.domain.Board;
import board.backend.board.domain.BoardNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.util.List;

@NamedInterface
@RequiredArgsConstructor
@Component
public class BoardReader {

    private final BoardRepository boardRepository;

    public void checkBoardExistsOrThrow(Long boardId) {
        if (!boardRepository.existsById(boardId)) {
            throw new BoardNotFound();
        }
    }

    public List<Board> readAll() {
        return boardRepository.findAll();
    }

}
