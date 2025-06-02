package board.backend.infra;

import board.backend.domain.ArticleLike;
import board.backend.domain.ArticleLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, ArticleLikeId>, CustomArticleLikeRepository {

    Optional<ArticleLike> findByArticleIdAndUserId(Long articleId, Long userId);

    void deleteByArticleId(Long articleId);

}
