package board.backend.comment.infra;

import board.backend.comment.domain.ArticleCommentCount;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCommentCountRepository extends JpaRepository<ArticleCommentCount, Long>, CustomArticleCommentCountRepository {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO article_comment_count (article_id, comment_count)
        VALUES (:articleId, :commentCount)
        ON DUPLICATE KEY UPDATE comment_count = comment_count + 1
        """, nativeQuery = true)
    void increaseOrSave(@Param("articleId") Long articleId, @Param("commentCount") Long commentCount);

}
