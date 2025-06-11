package board.backend.like.infra;

import board.backend.like.domain.ArticleLikeCount;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleLikeCountRepository extends JpaRepository<ArticleLikeCount, Long>, CustomArticleLikeCountRepository {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO article_like_count (article_id, like_count)
        VALUES (:articleId, :likeCount)
        ON DUPLICATE KEY UPDATE like_count = like_count + 1
        """, nativeQuery = true)
    void increaseOrSave(@Param("articleId") Long articleId, @Param("likeCount") Long likeCount);

}
