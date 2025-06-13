package board.backend.like.application.port;

import board.backend.like.domain.ArticleLikeCount;

import java.util.List;

public interface ArticleLikeCountRepository {

    List<ArticleLikeCount> findAllById(List<Long> articleIds);

    void deleteById(Long articleId);
    
    void increaseOrSave(Long articleId, Long likeCount);

    void decrease(Long articleId);

}
