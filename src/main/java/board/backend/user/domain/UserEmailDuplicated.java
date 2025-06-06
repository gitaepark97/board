package board.backend.user.domain;

import board.backend.common.support.ApplicationException;

public class UserEmailDuplicated extends ApplicationException {

    public UserEmailDuplicated() {
        super(409, "이미 사용 중인 이메일입니다.");
    }

}
