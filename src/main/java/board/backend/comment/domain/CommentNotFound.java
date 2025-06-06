package board.backend.comment.domain;

import board.backend.common.support.ApplicationException;

public class CommentNotFound extends ApplicationException {

    public CommentNotFound() {
        super(404, "존재하지 않는 댓글입니다.");
    }

}
