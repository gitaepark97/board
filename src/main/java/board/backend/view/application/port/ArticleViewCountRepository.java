package board.backend.view.application.port;

import board.backend.view.domain.ArticleViewCount;

import java.util.List;

public interface ArticleViewCountRepository {

    List<ArticleViewCount> findAllById(List<Long> articleIds);

    void save(ArticleViewCount articleViewCount);

    void deleteById(Long articleId);

    void increase(Long articleId);

}
