package board.backend.common.exception;

public class InternalServerError extends ApplicationException {

    public InternalServerError() {
        super(500, "서버 오류입니다.");
    }

}
