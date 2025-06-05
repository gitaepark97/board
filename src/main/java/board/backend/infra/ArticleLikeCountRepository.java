package board.backend.infra;

import board.backend.domain.ArticleLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleLikeCountRepository extends JpaRepository<ArticleLikeCount, Long>, CustomArticleLikeCountRepository {

}
