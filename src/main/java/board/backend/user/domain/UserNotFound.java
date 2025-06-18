package board.backend.user.domain;

import board.backend.common.exception.ApplicationException;

public class UserNotFound extends ApplicationException {

    public UserNotFound() {
        super(404, "존재하지 않는 회원입니다.");
    }

}
