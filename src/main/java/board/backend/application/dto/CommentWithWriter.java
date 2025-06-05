package board.backend.application.dto;

import board.backend.domain.Comment;
import board.backend.domain.User;

public record CommentWithWriter(
    Comment comment,
    User writer
) {

    public static CommentWithWriter of(Comment comment, User writer) {
        return new CommentWithWriter(comment, writer);
    }

}
