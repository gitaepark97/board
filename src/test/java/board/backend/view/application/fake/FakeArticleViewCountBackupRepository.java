package board.backend.view.application.fake;

import board.backend.view.application.port.ArticleViewCountBackupRepository;
import board.backend.view.domain.ArticleViewCount;

import java.util.*;

public class FakeArticleViewCountBackupRepository implements ArticleViewCountBackupRepository {

    private final Map<Long, ArticleViewCount> store = new HashMap<>();

    @Override
    public Optional<ArticleViewCount> findById(Long articleId) {
        return Optional.ofNullable(store.get(articleId));
    }

    @Override
    public List<ArticleViewCount> findAllById(List<Long> articleIds) {
        return articleIds.stream()
            .map(store::get)
            .filter(Objects::nonNull)
            .toList();
    }

    @Override
    public void save(ArticleViewCount articleViewCount) {
        store.put(articleViewCount.articleId(), articleViewCount);
    }

    @Override
    public void deleteById(Long articleId) {
        store.remove(articleId);
    }

}
