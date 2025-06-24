package board.backend.common.count.application.port;

import board.backend.common.count.domain.ArticleCount;

import java.util.List;
import java.util.Optional;

public interface ArticleCountRepository<T extends ArticleCount> {

    Optional<T> findById(Long articleId);

    List<T> findAll();

    List<T> findAllById(List<Long> articleIds);

    void deleteById(Long articleId);

}
