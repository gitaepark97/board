package board.backend.like.application.port;

import board.backend.common.count.application.port.ArticleCountRepository;
import board.backend.like.domain.ArticleLikeCount;

public interface ArticleLikeCountRepository extends ArticleCountRepository<ArticleLikeCount> {

    void increase(Long articleId);

    void decrease(Long articleId);

}
