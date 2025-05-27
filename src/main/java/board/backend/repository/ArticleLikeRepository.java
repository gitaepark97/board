package board.backend.repository;

import board.backend.domain.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long> {

}
