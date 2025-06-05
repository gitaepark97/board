package board.backend.domain;

import board.backend.support.ApplicationException;

public class LoginInfoNotFound extends ApplicationException {

    public LoginInfoNotFound() {
        super(404, "존재하지 않는 로그인 정보입니다.");
    }

}
