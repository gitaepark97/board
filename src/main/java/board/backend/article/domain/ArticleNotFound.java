package board.backend.article.domain;

import board.backend.common.support.ApplicationException;

public class ArticleNotFound extends ApplicationException {

    public ArticleNotFound() {
        super(404, "존재하지 않는 게시글입니다.");
    }

}
