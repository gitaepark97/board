package board.backend.comment.application;

import board.backend.comment.application.dto.CommentWithWriter;
import board.backend.comment.application.port.ArticleCommentCountRepository;
import board.backend.comment.application.port.CommentRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.Comment;
import board.backend.common.infra.CachedRepository;
import board.backend.user.application.UserReader;
import board.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@NamedInterface
@RequiredArgsConstructor
@Component
public class CommentReader {

    private final static Duration CACHE_TTL = Duration.ofMinutes(5);

    private final CommentRepository commentRepository;
    private final CachedRepository<ArticleCommentCount, Long> cachedArticleCommentCountRepository;
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

        List<Long> writerIds = comments.stream().map(Comment::writerId).toList();

        // 작성자 조회
        Map<Long, User> writerMap = userReader.readAll(writerIds);

        return comments.stream()
            .map(comment -> CommentWithWriter.of(comment, writerMap.get(comment.writerId())))
            .toList();
    }

    public Long count(Long articleId) {
        return cachedArticleCommentCountRepository.findByKey(articleId)
            .or(() -> articleCommentCountRepository.findById(articleId))
            .map(ArticleCommentCount::commentCount)
            .orElse(0L);
    }

    public Map<Long, Long> count(List<Long> articleIds) {
        // 캐시 조회
        List<ArticleCommentCount> cached = cachedArticleCommentCountRepository.findAllByKey(articleIds);

        Map<Long, Long> map = cached.stream()
            .collect(Collectors.toMap(ArticleCommentCount::articleId, ArticleCommentCount::commentCount));

        // 캐시 미스만 조회
        List<Long> missed = articleIds.stream()
            .filter(not(map::containsKey))
            .peek(id -> map.put(id, 0L))
            .toList();
        if (!missed.isEmpty()) {
            List<ArticleCommentCount> uncached = articleCommentCountRepository.findAllById(missed);

            // 캐시에 저장
            uncached.forEach(articleCommentCount -> cachedArticleCommentCountRepository.save(articleCommentCount.articleId(), articleCommentCount, CACHE_TTL));

            // 합쳐서 반환
            uncached.forEach(articleCommentCount -> map.put(articleCommentCount.articleId(), articleCommentCount.commentCount()));
        }

        return map;
    }

}
