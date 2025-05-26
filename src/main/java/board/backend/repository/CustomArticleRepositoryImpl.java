package board.backend.repository;

import board.backend.domain.Article;
import board.backend.domain.QArticle;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class CustomArticleRepositoryImpl implements CustomArticleRepository {

    private final JPAQueryFactory queryFactory;

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
    public List<Article> findAllByBoardId(Long boardId, Long pageSize, Long lastArticleId) {
        QArticle article = QArticle.article;

        return queryFactory
            .selectFrom(article)
            .where(
                article.boardId.eq(boardId),
                article.id.lt(lastArticleId)
            )
            .orderBy(article.id.desc())
            .limit(pageSize)
            .fetch();
    }

}
