package board.backend.article.infra.jpa;

import board.backend.article.application.port.ArticleRepository;
import board.backend.article.domain.Article;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class ArticleRepositoryImpl implements ArticleRepository {

    private final JPAQueryFactory queryFactory;
    private final ArticleEntityRepository articleEntityRepository;

    @Override
    public boolean existsById(Long id) {
        QArticleEntity articleEntity = QArticleEntity.articleEntity;

        Integer result = queryFactory
            .selectOne()
            .from(articleEntity)
            .where(articleEntity.id.eq(id))
            .fetchFirst();

        return result != null;
    }

    @Override
    public Optional<Article> findById(Long id) {
        return articleEntityRepository.findById(id).map(ArticleEntity::toArticle);
    }

    @Override
    public List<Article> findAllByBoardId(Long boardId, Long pageSize) {
        QArticleEntity articleEntity = QArticleEntity.articleEntity;

        return queryFactory
            .selectFrom(articleEntity)
            .where(
                articleEntity.boardId.eq(boardId)
            )
            .orderBy(articleEntity.id.desc())
            .limit(pageSize)
            .fetch()
            .stream()
            .map(ArticleEntity::toArticle)
            .toList();
    }

    @Override
    public List<Article> findAllByBoardId(Long boardId, Long pageSize, Long lastId) {
        QArticleEntity articleEntity = QArticleEntity.articleEntity;

        return queryFactory
            .selectFrom(articleEntity)
            .where(
                articleEntity.boardId.eq(boardId),
                articleEntity.id.lt(lastId)
            )
            .orderBy(articleEntity.id.desc())
            .limit(pageSize)
            .fetch()
            .stream()
            .map(ArticleEntity::toArticle)
            .toList();
    }

    @Override
    public void save(Article article) {
        articleEntityRepository.save(ArticleEntity.from(article));
    }

    @Override
    public void delete(Article article) {
        articleEntityRepository.delete(ArticleEntity.from(article));
    }

}
