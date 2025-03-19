package example.hugo.infra.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleLikeCountEntityRepository extends JpaRepository<ArticleLikeCountEntity, Long> {

}
