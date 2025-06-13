package board.backend.like.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface ArticleLikeEntityRepository extends JpaRepository<ArticleLikeEntity, Long> {

    Optional<ArticleLikeEntity> findByArticleIdAndUserId(Long articleId, Long userId);

    void deleteByArticleId(Long articleId);

}
