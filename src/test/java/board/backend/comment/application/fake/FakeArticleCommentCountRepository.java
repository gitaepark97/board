package board.backend.comment.application.fake;

import board.backend.comment.application.port.ArticleCommentCountRepository;
import board.backend.comment.domain.ArticleCommentCount;

import java.util.*;

public class FakeArticleCommentCountRepository implements ArticleCommentCountRepository {

    private final Map<Long, ArticleCommentCount> store = new HashMap<>();

    @Override
    public Optional<ArticleCommentCount> findById(Long articleId) {
        return Optional.ofNullable(store.get(articleId));
    }

    @Override
    public List<ArticleCommentCount> findAllById(List<Long> articleIds) {
        List<ArticleCommentCount> result = new ArrayList<>();
        for (Long id : articleIds) {
            ArticleCommentCount count = store.get(id);
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
    public void increaseOrSave(ArticleCommentCount articleCommentCount) {
        Long articleId = articleCommentCount.articleId();
        ArticleCommentCount existing = store.get(articleId);

        if (existing == null) {
            store.put(articleId, new ArticleCommentCount(articleId, 1L));
        } else {
            store.put(articleId, new ArticleCommentCount(articleId, existing.commentCount() + 1));
        }
    }

    @Override
    public void decrease(Long articleId) {
        ArticleCommentCount existing = store.get(articleId);
        if (existing != null) {
            store.put(articleId, new ArticleCommentCount(articleId, existing.commentCount() - 1));
        }
    }

    public void save(ArticleCommentCount articleCommentCount) {
        store.put(articleCommentCount.articleId(), articleCommentCount);
    }

}