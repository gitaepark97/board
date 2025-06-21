package board.backend.like.application.fake;

import board.backend.like.application.port.ArticleLikeCountRepository;
import board.backend.like.domain.ArticleLikeCount;

import java.util.*;

public class FakeArticleCommentRepository implements ArticleLikeCountRepository {

    private final Map<Long, ArticleLikeCount> store = new HashMap<>();

    @Override
    public Optional<ArticleLikeCount> findById(Long articleId) {
        return Optional.ofNullable(store.get(articleId));
    }

    @Override
    public List<ArticleLikeCount> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<ArticleLikeCount> findAllById(List<Long> articleIds) {
        List<ArticleLikeCount> result = new ArrayList<>();
        for (Long id : articleIds) {
            ArticleLikeCount count = store.get(id);
            if (count != null) {
                result.add(count);
            }
        }
        return result;
    }

    @Override
    public void deleteById(Long articleId) {
        store.remove(articleId);
    }

    @Override
    public void increaseOrSave(ArticleLikeCount articleLikeCount) {
        Long articleId = articleLikeCount.articleId();
        ArticleLikeCount existing = store.get(articleId);

        if (existing == null) {
            store.put(articleId, articleLikeCount);
        } else {
            store.put(articleId, new ArticleLikeCount(articleId, existing.likeCount() + 1));
        }
    }

    @Override
    public void decrease(Long articleId) {
        ArticleLikeCount existing = store.get(articleId);

        if (existing != null && existing.likeCount() > 0) {
            store.put(articleId, new ArticleLikeCount(articleId, existing.likeCount() - 1));
        }
    }

    public void save(ArticleLikeCount articleLikeCount) {
        store.put(articleLikeCount.articleId(), articleLikeCount);
    }

}
