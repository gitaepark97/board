package board.backend.board.web.response;

import board.backend.board.domain.Board;

public record BoardResponse(
    String id,
    String title
) {

    public static BoardResponse from(Board board) {
        return new BoardResponse(board.id().toString(), board.title());
    }

}
