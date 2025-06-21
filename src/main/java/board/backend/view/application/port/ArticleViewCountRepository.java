package board.backend.view.application.port;

import board.backend.view.domain.ArticleViewCount;

import java.util.List;
import java.util.Map;

public interface ArticleViewCountRepository {

    Long findById(Long articleId);

    List<ArticleViewCount> findAll();

    Map<Long, Long> findAllById(List<Long> articleIds);

    void deleteById(Long articleId);

    Long increase(Long articleId);

    void save(ArticleViewCount articleViewCount);

}
