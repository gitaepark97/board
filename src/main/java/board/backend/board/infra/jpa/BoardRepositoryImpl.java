package board.backend.board.infra.jpa;

import board.backend.board.application.port.BoardRepository;
import board.backend.board.domain.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class BoardRepositoryImpl implements BoardRepository {

    private final JPAQueryFactory queryFactory;
    private final BoardEntityRepository boardEntityRepository;

    @Override
    public boolean existsById(Long id) {
        QBoardEntity boardEntity = QBoardEntity.boardEntity;

        Integer result = queryFactory
            .selectOne()
            .from(boardEntity)
            .where(boardEntity.id.eq(id))
            .fetchFirst();

        return result != null;
    }

    @Override
    public List<Board> findAll() {
        return boardEntityRepository.findAll().stream().map(BoardEntity::toBoard).collect(Collectors.toList());
    }

}
