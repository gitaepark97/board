package board.backend.article.application.fake;

import board.backend.article.application.port.ArticleRepository;
import board.backend.article.domain.Article;

import java.util.*;

public class FakeArticleRepository implements ArticleRepository {

    private final Map<Long, Article> store = new HashMap<>();

    @Override
    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    @Override
    public Optional<Article> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Article> findAllById(List<Long> ids) {
        return ids.stream()
            .map(store::get)
            .filter(Objects::nonNull)
            .toList();
    }

    @Override
    public List<Article> findAllByBoardId(Long boardId, Long pageSize) {
        return store.values().stream()
            .filter(article -> article.boardId().equals(boardId))
            .sorted(Comparator.comparing(Article::id).reversed())
            .limit(pageSize)
            .toList();
    }

    @Override
    public List<Article> findAllByBoardId(Long boardId, Long pageSize, Long lastId) {
        return store.values().stream()
            .filter(article -> article.boardId().equals(boardId) && article.id() < lastId)
            .sorted(Comparator.comparing(Article::id).reversed())
            .limit(pageSize)
            .toList();
    }

    @Override
    public void save(Article article) {
        store.put(article.id(), article);
    }

    @Override
    public void delete(Article article) {
        store.remove(article.id());
    }

}