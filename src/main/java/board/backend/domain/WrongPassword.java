package board.backend.domain;

import board.backend.support.ApplicationException;
import org.springframework.http.HttpStatus;

public class WrongPassword extends ApplicationException {

    public WrongPassword() {
        super(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다.");
    }

}
