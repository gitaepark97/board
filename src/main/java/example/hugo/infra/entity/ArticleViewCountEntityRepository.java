package example.hugo.infra.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleViewCountEntityRepository extends JpaRepository<ArticleViewCountEntity, Long> {

}
