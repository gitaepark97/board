package board.backend.article.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

interface ArticleEntityRepository extends JpaRepository<ArticleEntity, Long> {

}
