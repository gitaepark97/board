package board.backend.domain;

import board.backend.support.ApplicationException;
import org.springframework.http.HttpStatus;

public class SessionInvalid extends ApplicationException {

    public SessionInvalid() {
        super(HttpStatus.UNAUTHORIZED, "유효하지 않은 세션입니다.");
    }

}
