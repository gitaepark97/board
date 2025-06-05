package board.backend.domain;

import board.backend.support.ApplicationException;

public class WrongPassword extends ApplicationException {

    public WrongPassword() {
        super(401, "잘못된 비밀번호입니다.");
    }

}
