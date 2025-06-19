package board.backend.comment.application.fake;

import board.backend.comment.application.port.CommentRepository;
import board.backend.comment.domain.Comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FakeCommentRepository implements CommentRepository {

    private final Map<Long, Comment> store = new HashMap<>();
    private final Map<String, Integer> countByMap = new HashMap<>();

    @Override
    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    @Override
    public int countBy(Long articleId, Long parentId, Integer limit) {
        String key = generateKey(articleId, parentId, limit);
        return countByMap.getOrDefault(key, 0);
    }

    private String generateKey(Long articleId, Long parentId, Integer limit) {
        return articleId + ":" + parentId + ":" + limit;
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Comment> findAllById(Long articleId, Long pageSize) {
        return store.values().stream()
            .filter(c -> c.articleId().equals(articleId))
            .limit(pageSize)
            .toList();
    }

    @Override
    public List<Comment> findAllById(Long articleId, Long pageSize, Long lastParentId, Long lastId) {
        return store.values().stream()
            .filter(c -> c.articleId().equals(articleId))
            .filter(c -> c.id() > lastId)
            .limit(pageSize)
            .toList();
    }

    @Override
    public void save(Comment comment) {
        store.put(comment.id(), comment);
    }

    @Override
    public void delete(Comment comment) {
        store.remove(comment.id());
    }

    @Override
    public void deleteByByArticleId(Long articleId) {
        store.values().removeIf(c -> c.articleId().equals(articleId));
    }

    public void setCountBy(Long articleId, Long parentId, Integer limit, int count) {
        countByMap.put(generateKey(articleId, parentId, limit), count);
    }

}