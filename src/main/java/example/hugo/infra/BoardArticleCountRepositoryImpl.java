package example.hugo.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import example.hugo.application.BoardArticleCountRepository;
import example.hugo.domain.BoardArticleCount;
import example.hugo.infra.entity.BoardArticleCountEntity;
import example.hugo.infra.entity.BoardArticleCountEntityRepository;
import example.hugo.infra.entity.QBoardArticleCountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
class BoardArticleCountRepositoryImpl implements BoardArticleCountRepository {

    private final BoardArticleCountEntityRepository boardArticleCountEntityRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<BoardArticleCount> findById(Long boardId) {
        return boardArticleCountEntityRepository.findById(boardId).map(BoardArticleCountEntity::toBoardArticleCount);
    }

    @Override
    public void save(BoardArticleCount boardArticleCount) {
        boardArticleCountEntityRepository.save(BoardArticleCountEntity.from(boardArticleCount));
    }

    @Override
    public long increase(Long boardId) {
        QBoardArticleCountEntity boardArticleCountEntity = QBoardArticleCountEntity.boardArticleCountEntity;

        return queryFactory
            .update(boardArticleCountEntity)
            .set(boardArticleCountEntity.articleCount, boardArticleCountEntity.articleCount.add(1))
            .where(boardArticleCountEntity.boardId.eq(boardId))
            .execute();
    }

    @Override
    public void decrease(Long boardId) {
        QBoardArticleCountEntity boardArticleCountEntity = QBoardArticleCountEntity.boardArticleCountEntity;

        queryFactory
            .update(boardArticleCountEntity)
            .set(boardArticleCountEntity.articleCount, boardArticleCountEntity.articleCount.subtract(1))
            .where(boardArticleCountEntity.boardId.eq(boardId))
            .execute();
    }

}
