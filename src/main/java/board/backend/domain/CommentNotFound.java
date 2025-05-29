package board.backend.domain;

import board.backend.support.ApplicationException;
import org.springframework.http.HttpStatus;

public class CommentNotFound extends ApplicationException {

    public CommentNotFound() {
        super(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다.");
    }

}
