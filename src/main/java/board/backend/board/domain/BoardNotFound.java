package board.backend.board.domain;

import board.backend.common.support.ApplicationException;

public class BoardNotFound extends ApplicationException {

    public BoardNotFound() {
        super(404, "존재하지 않는 게시판입니다.");
    }

}
