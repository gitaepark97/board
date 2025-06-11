package board.backend.comment.application;

import board.backend.comment.application.dto.CommentWithWriter;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.Comment;
import board.backend.comment.infra.ArticleCommentCountRepository;
import board.backend.comment.infra.CommentRepository;
import board.backend.common.infra.CacheRepository;
import board.backend.user.application.UserReader;
import board.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CommentReader {

    private final static Duration CACHE_TTL = Duration.ofMinutes(5);

    private final CommentRepository commentRepository;
    private final CacheRepository<ArticleCommentCount, Long> articleCommentCountCacheRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;
    private final UserReader userReader;

    public List<CommentWithWriter> readAll(
        Long articleId,
        Long pageSize,
        Long lastParentCommentId,
        Long lastCommentId
    ) {
        List<Comment> comments = lastParentCommentId == null || lastCommentId == null ?
            commentRepository.findAllById(articleId, pageSize) :
            commentRepository.findAllById(articleId, pageSize, lastParentCommentId, lastCommentId);

        List<Long> writerIds = comments.stream().map(Comment::getWriterId).toList();

        // 작성자 조회
        Map<Long, User> writerMap = userReader.readAll(writerIds);

        return comments.stream()
            .map(comment -> CommentWithWriter.of(comment, writerMap.get(comment.getWriterId())))
            .collect(Collectors.toList());
    }

    public Map<Long, Long> count(List<Long> articleIds) {
        // 캐시 조회
        List<ArticleCommentCount> cached = articleCommentCountCacheRepository.getAll(articleIds);

        Map<Long, Long> map = cached.stream()
            .collect(Collectors.toMap(ArticleCommentCount::getArticleId, ArticleCommentCount::getCommentCount));

        // 캐시 미스만 조회
        List<Long> missed = articleIds.stream()
            .filter(id -> !map.containsKey(id))
            .toList();
        if (!missed.isEmpty()) {
            List<ArticleCommentCount> uncached = articleCommentCountRepository.findAllById(missed);

            // 캐시에 저장
            uncached.forEach(articleCommentCount -> articleCommentCountCacheRepository.set(articleCommentCount.getArticleId(), articleCommentCount, CACHE_TTL));

            // 합쳐서 반환
            uncached.forEach(articleCommentCount -> map.put(articleCommentCount.getArticleId(), articleCommentCount.getCommentCount()));
        }

        return map;
    }

}
