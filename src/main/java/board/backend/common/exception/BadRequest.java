package board.backend.common.exception;

public class BadRequest extends ApplicationException {

    public BadRequest() {
        super(400, "잘못된 입력입니다.");
    }

}
