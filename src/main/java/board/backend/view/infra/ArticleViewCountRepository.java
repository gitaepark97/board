package board.backend.view.infra;

import board.backend.view.domain.ArticleViewCount;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleViewCountRepository extends JpaRepository<ArticleViewCount, Long> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO article_view_count (article_id, view_count)
        VALUES (:articleId, :viewCount)
        ON DUPLICATE KEY UPDATE view_count = view_count + 1
        """, nativeQuery = true)
    void upsert(@Param("articleId") Long articleId, @Param("viewCount") Long viewCount);

}
