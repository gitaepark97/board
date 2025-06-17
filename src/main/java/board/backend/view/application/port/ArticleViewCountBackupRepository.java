package board.backend.view.application.port;

import board.backend.view.domain.ArticleViewCount;

import java.util.List;
import java.util.Optional;

public interface ArticleViewCountBackupRepository {

    Optional<ArticleViewCount> findById(Long articleId);

    List<ArticleViewCount> findAllById(List<Long> articleIds);

    void save(ArticleViewCount articleViewCount);

    void deleteById(Long articleId);

}
