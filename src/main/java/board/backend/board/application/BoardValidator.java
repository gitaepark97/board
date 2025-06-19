package board.backend.board.application;

import board.backend.board.application.port.BoardRepository;
import board.backend.board.domain.BoardNotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

@NamedInterface
@RequiredArgsConstructor
@Component
public class BoardValidator {

    private final BoardRepository boardRepository;

    public void checkBoardExistsOrThrow(Long boardId) {
        if (!boardRepository.existsById(boardId)) {
            throw new BoardNotFound();
        }
    }

}
