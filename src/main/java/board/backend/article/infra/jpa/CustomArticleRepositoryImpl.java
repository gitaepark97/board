package board.backend.article.infra.jpa;

import board.backend.article.domain.Article;
import board.backend.article.domain.QArticle;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class CustomArticleRepositoryImpl implements CustomArticleRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public boolean customExistsById(Long id) {
        QArticle article = QArticle.article;

        Integer result = queryFactory
            .selectOne()
            .from(article)
            .where(article.id.eq(id))
            .fetchFirst();

        return result != null;
    }

    @Override
    public List<Article> findAllByBoardId(Long boardId, Long pageSize) {
        QArticle article = QArticle.article;

        return queryFactory
            .selectFrom(article)
            .where(
                article.boardId.eq(boardId)
            )
            .orderBy(article.id.desc())
            .limit(pageSize)
            .fetch();
    }

    @Override
    public List<Article> findAllByBoardId(Long boardId, Long pageSize, Long lastId) {
        QArticle article = QArticle.article;

        return queryFactory
            .selectFrom(article)
            .where(
                article.boardId.eq(boardId),
                article.id.lt(lastId)
            )
            .orderBy(article.id.desc())
            .limit(pageSize)
            .fetch();
    }

}
