package board.backend.comment.application;

import board.backend.comment.application.dto.CommentWithWriter;
import board.backend.comment.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CacheManager cacheManager;
    private final CommentReader commentReader;
    private final CommentCreator commentCreator;
    private final CommentDeleter commentDeleter;

    @Cacheable(
        value = "comment::list::article",
        key = "#articleId",
        condition = "#pageSize.equals(10L) && #lastParentCommentId == null && #lastCommentId == null"
    )
    public List<CommentWithWriter> readAll(
        Long articleId,
        Long pageSize,
        Long lastParentCommentId,
        Long lastCommentId
    ) {
        return commentReader.readAll(articleId, pageSize, lastParentCommentId, lastCommentId);
    }

    @CacheEvict(
        value = "comment::list::article",
        key = "#articleId"
    )
    public Comment create(Long articleId, Long userId, Long parentCommentId, String content) {
        return commentCreator.create(articleId, userId, parentCommentId, content);
    }

    public void delete(Long commentId, Long userId) {
        Optional<Long> articleId = commentDeleter.delete(commentId, userId);
        articleId.ifPresent(id -> {
            Cache cache = cacheManager.getCache("comment::list::article");
            if (cache != null) {
                cache.evict(id);
            }
        });
    }

}
