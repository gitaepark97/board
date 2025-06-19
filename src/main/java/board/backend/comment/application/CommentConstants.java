package board.backend.comment.application;

import java.time.Duration;

abstract class CommentConstants {

    final static Duration COMMENT_COUNT_CACHE_TTL = Duration.ofMinutes(5);

}
