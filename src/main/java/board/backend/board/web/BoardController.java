package board.backend.board.web;

import board.backend.board.application.BoardService;
import board.backend.board.web.response.BoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/boards")
class BoardController {

    private final BoardService boardService;

    @GetMapping
    List<BoardResponse> readAll() {
        return boardService.readAll().stream().map(BoardResponse::from).toList();
    }

}
