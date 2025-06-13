package board.backend.board.infra.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

interface BoardEntityRepository extends JpaRepository<BoardEntity, Long> {

}
