package board.backend.like.infra.jpa;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

interface ArticleLikeCountEntityRepository extends JpaRepository<ArticleLikeCountEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO article_like_count (article_id, like_count)
        VALUES (:articleId, 1)
        ON DUPLICATE KEY UPDATE like_count = like_count + 1
        """, nativeQuery = true)
    void increaseOrSave(@Param("articleId") Long articleId);

}
