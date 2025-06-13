package board.backend.comment.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {

    void deleteByArticleId(Long articleId);
    
}
