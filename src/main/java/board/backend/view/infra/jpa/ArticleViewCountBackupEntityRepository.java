package board.backend.view.infra.jpa;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface ArticleViewCountBackupEntityRepository extends JpaRepository<ArticleViewCountBackupEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO article_view_count (article_id, view_count)
        VALUES (:articleId, :viewCount)
        ON DUPLICATE KEY UPDATE view_count = :viewCount
        """, nativeQuery = true)
    void upsert(@Param("articleId") Long articleId, @Param("increment") Long viewCount);

}
