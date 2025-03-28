package example.hugo.infra.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikeEntityRepository extends JpaRepository<ArticleLikeEntity, Long> {

    Optional<ArticleLikeEntity> findByArticleIdAndUserId(Long articleId, Long userId);

}
