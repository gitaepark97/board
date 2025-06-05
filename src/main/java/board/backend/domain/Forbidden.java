package board.backend.domain;

import board.backend.support.ApplicationException;
import org.springframework.http.HttpStatus;

public class Forbidden extends ApplicationException {

    public Forbidden() {
        super(HttpStatus.FORBIDDEN, "권한이 없습니다.");
    }

}
