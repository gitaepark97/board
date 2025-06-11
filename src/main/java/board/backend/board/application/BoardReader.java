package board.backend.board.application;

import board.backend.board.domain.Board;
import board.backend.board.domain.BoardNotFound;
import board.backend.board.infra.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class BoardReader {

    private final BoardRepository boardRepository;

    public void checkBoardExistsOrThrow(Long boardId) {
        if (!boardRepository.customExistsById(boardId)) {
            throw new BoardNotFound();
        }
    }

    public List<Board> readAll() {
        return boardRepository.findAll();
    }

}
