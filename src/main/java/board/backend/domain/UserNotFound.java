package board.backend.domain;

import board.backend.support.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserNotFound extends ApplicationException {

    public UserNotFound() {
        super(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다.");
    }

}
