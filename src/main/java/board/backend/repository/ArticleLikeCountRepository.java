package board.backend.repository;

import board.backend.domain.ArticleLikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ArticleLikeCountRepository extends JpaRepository<ArticleLikeCount, Long>, CustomArticleLikeCountRepository {

    List<ArticleLikeCount> findByArticleIdIn(Collection<Long> articleIds);

}
