package board.backend.view.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

interface ArticleViewCountEntityRepository extends JpaRepository<ArticleViewCountEntity, Long> {

}
