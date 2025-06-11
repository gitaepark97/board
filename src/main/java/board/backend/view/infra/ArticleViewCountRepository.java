package board.backend.view.infra;

import board.backend.view.domain.ArticleViewCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleViewCountRepository extends JpaRepository<ArticleViewCount, Long>, CustomArticleViewCountRepository {

}
