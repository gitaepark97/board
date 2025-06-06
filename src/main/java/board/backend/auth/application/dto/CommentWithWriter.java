package board.backend.auth.application.dto;

import board.backend.comment.domain.Comment;
import board.backend.user.domain.User;

public record CommentWithWriter(
    Comment comment,
    User writer
) {

    public static CommentWithWriter of(Comment comment, User writer) {
        return new CommentWithWriter(comment, writer);
    }

}
