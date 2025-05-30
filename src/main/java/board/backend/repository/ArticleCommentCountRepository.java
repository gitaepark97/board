package board.backend.repository;

import board.backend.domain.ArticleCommentCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleCommentCountRepository extends JpaRepository<ArticleCommentCount, Long>, CustomArticleCommentCountRepository {

    List<ArticleCommentCount> findByArticleIdIn(List<Long> articleIds);

}
