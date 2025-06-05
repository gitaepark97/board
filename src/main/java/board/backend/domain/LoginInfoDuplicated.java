package board.backend.domain;

import board.backend.support.ApplicationException;

public class LoginInfoDuplicated extends ApplicationException {

    public LoginInfoDuplicated() {
        super(409, "이미 존재하는 로그인 정보입니다.");
    }

}
