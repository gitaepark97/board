package board.backend.auth.domain;

import board.backend.common.exception.ApplicationException;

public class SessionInvalid extends ApplicationException {

    public SessionInvalid() {
        super(401, "유효하지 않은 세션입니다.");
    }

}
