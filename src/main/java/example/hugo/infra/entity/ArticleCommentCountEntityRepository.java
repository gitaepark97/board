package example.hugo.infra.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleCommentCountEntityRepository extends JpaRepository<ArticleCommentCountEntity, Long> {

}
