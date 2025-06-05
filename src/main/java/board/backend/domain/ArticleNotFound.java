package board.backend.domain;

import board.backend.support.ApplicationException;

public class ArticleNotFound extends ApplicationException {

    public ArticleNotFound() {
        super(404, "존재하지 않는 게시글입니다.");
    }

}
