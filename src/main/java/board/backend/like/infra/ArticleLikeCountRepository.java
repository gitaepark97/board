package board.backend.like.infra;

import board.backend.like.domain.ArticleLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleLikeCountRepository extends JpaRepository<ArticleLikeCount, Long>, CustomArticleLikeCountRepository {

}
