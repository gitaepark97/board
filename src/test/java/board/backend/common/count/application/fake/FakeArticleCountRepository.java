package board.backend.common.count.application.fake;

import board.backend.common.count.application.port.ArticleCountRepository;
import board.backend.common.count.domain.ArticleCount;

import java.util.*;

public class FakeArticleCountRepository<T extends ArticleCount> implements ArticleCountRepository<T> {

    protected final Map<Long, T> store = new HashMap<>();

    @Override
    public Optional<T> findById(Long articleId) {
        return Optional.ofNullable(store.get(articleId));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<T> findAllById(List<Long> articleIds) {
        return articleIds.stream()
            .map(store::get)
            .filter(Objects::nonNull)
            .toList();
    }

    @Override
    public void deleteById(Long articleId) {
        store.remove(articleId);
    }

    public void save(T count) {
        store.put(count.getArticleId(), count);
    }

}
