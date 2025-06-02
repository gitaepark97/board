package board.backend.domain;

import board.backend.support.ApplicationException;
import org.springframework.http.HttpStatus;

public class LoginInfoDuplicated extends ApplicationException {

    public LoginInfoDuplicated() {
        super(HttpStatus.CONFLICT, "이미 존재하는 로그인 정보입니다.");
    }

}
