package board.backend.domain;

import board.backend.support.ApplicationException;
import org.springframework.http.HttpStatus;

public class UserEmailDuplicated extends ApplicationException {

    public UserEmailDuplicated() {
        super(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다.");
    }

}
