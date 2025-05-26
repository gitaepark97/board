package board.backend.domain;

import board.backend.support.ApplicationException;
import org.springframework.http.HttpStatus;

public class ArticleNotFound extends ApplicationException {

    public ArticleNotFound() {
        super(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다.");
    }

}
