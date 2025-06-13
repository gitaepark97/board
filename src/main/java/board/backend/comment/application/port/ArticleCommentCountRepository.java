package board.backend.comment.application.port;

import board.backend.comment.domain.ArticleCommentCount;

import java.util.List;

public interface ArticleCommentCountRepository {

    List<ArticleCommentCount> findAllById(List<Long> articleIds);

    void deleteById(Long articleId);

    void increaseOrSave(Long articleId, Long commentCount);

    void decrease(Long articleId);

}
