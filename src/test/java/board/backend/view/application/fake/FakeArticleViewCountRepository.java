package board.backend.view.application.fake;

import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeArticleViewCountRepository implements ArticleViewCountRepository {

    private final Map<Long, Long> store = new HashMap<>();

    @Override
    public Long findById(Long articleId) {
        return store.getOrDefault(articleId, 0L);
    }

    @Override
    public Map<Long, Long> findAllById(List<Long> articleIds) {
        Map<Long, Long> result = new HashMap<>();
        for (Long id : articleIds) {
            result.put(id, store.getOrDefault(id, 0L));
        }
        return result;
    }

    @Override
    public void deleteById(Long articleId) {
        store.remove(articleId);
    }

    @Override
    public Long increase(Long articleId) {
        return store.merge(articleId, 1L, Long::sum);
    }

    @Override
    public void save(ArticleViewCount articleViewCount) {
        store.put(articleViewCount.articleId(), articleViewCount.viewCount());
    }

}
