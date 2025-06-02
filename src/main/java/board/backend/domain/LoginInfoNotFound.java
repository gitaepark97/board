package board.backend.domain;

import board.backend.support.ApplicationException;
import org.springframework.http.HttpStatus;

public class LoginInfoNotFound extends ApplicationException {

    public LoginInfoNotFound() {
        super(HttpStatus.NOT_FOUND, "존재하지 않는 로그인 정보입니다.");
    }

}
