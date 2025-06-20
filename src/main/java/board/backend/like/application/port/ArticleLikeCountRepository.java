package board.backend.like.application.port;

import board.backend.like.domain.ArticleLikeCount;

import java.util.List;
import java.util.Optional;

public interface ArticleLikeCountRepository {

    Optional<ArticleLikeCount> findById(Long articleId);

    List<ArticleLikeCount> findAll();

    List<ArticleLikeCount> findAllById(List<Long> articleIds);

    void deleteById(Long articleId);

    void increaseOrSave(ArticleLikeCount articleLikeCount);

    void decrease(Long articleId);

}
