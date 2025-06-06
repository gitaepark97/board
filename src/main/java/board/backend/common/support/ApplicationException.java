package board.backend.common.support;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final int status;

    public ApplicationException(int status, String message) {
        super(message);
        this.status = status;
    }

}
