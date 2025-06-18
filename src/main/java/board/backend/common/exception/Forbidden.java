package board.backend.common.exception;

public class Forbidden extends ApplicationException {

    public Forbidden() {
        super(403, "권한이 없습니다.");
    }

}
