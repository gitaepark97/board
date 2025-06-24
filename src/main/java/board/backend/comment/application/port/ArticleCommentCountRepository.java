package board.backend.comment.application.port;

import board.backend.comment.domain.ArticleCommentCount;
import board.backend.common.count.application.port.ArticleCountRepository;

public interface ArticleCommentCountRepository extends ArticleCountRepository<ArticleCommentCount> {

    void increase(Long articleId);

    void decrease(Long articleId);

}
