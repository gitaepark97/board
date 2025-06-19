package board.backend.like.application.fake;

import board.backend.like.application.port.ArticleLikeRepository;
import board.backend.like.domain.ArticleLike;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class FakeArticleLikeRepository implements ArticleLikeRepository {

    private final Map<String, ArticleLike> store = new HashMap<>();

    private String key(Long articleId, Long userId) {
        return articleId + "::" + userId;
    }

    @Override
    public boolean existsByArticleIdAndUserId(Long articleId, Long userId) {
        return store.containsKey(key(articleId, userId));
    }

    @Override
    public Optional<ArticleLike> findByArticleIdAndUserId(Long articleId, Long userId) {
        return Optional.ofNullable(store.get(key(articleId, userId)));
    }

    @Override
    public void save(ArticleLike articleLike) {
        store.put(key(articleLike.articleId(), articleLike.userId()), articleLike);
    }

    @Override
    public void delete(ArticleLike articleLike) {
        store.remove(key(articleLike.articleId(), articleLike.userId()));
    }

    @Override
    public void deleteByArticleId(Long articleId) {
        store.entrySet().removeIf(entry -> entry.getValue().articleId().equals(articleId));
    }

}
