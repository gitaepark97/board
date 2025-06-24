package board.backend.view.application.port;

import board.backend.common.count.application.port.ArticleCountRepository;
import board.backend.view.domain.ArticleViewCount;

public interface ArticleViewCountRepository extends ArticleCountRepository<ArticleViewCount> {

    Long increase(Long articleId);

}
