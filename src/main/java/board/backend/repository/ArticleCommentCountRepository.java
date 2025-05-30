package board.backend.repository;

import board.backend.domain.ArticleCommentCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCommentCountRepository extends JpaRepository<ArticleCommentCount, Long>, CustomArticleCommentCountRepository {

}
