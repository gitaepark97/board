package board.backend.board.infra;

import board.backend.board.domain.QBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class CustomBoardRepositoryImpl implements CustomBoardRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean customExistsById(Long id) {
        QBoard board = QBoard.board;

        Integer result = queryFactory
            .selectOne()
            .from(board)
            .where(board.id.eq(id))
            .fetchFirst();

        return result != null;
    }

}
