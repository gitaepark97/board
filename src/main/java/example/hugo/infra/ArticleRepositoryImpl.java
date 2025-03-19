package example.hugo.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import example.hugo.application.ArticleRepository;
import example.hugo.domain.Article;
import example.hugo.infra.entity.ArticleEntity;
import example.hugo.infra.entity.ArticleEntityRepository;
import example.hugo.infra.entity.QArticleEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
class ArticleRepositoryImpl implements ArticleRepository {

    private final ArticleEntityRepository articleEntityRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsById(Long articleId) {
        return articleEntityRepository.existsById(articleId);
    }

    @Override
    public Optional<Article> findById(Long articleId) {
        return articleEntityRepository.findById(articleId).map(ArticleEntity::toArticle);
    }

    @Override
    public List<Article> findAllByBoardId(Long boardId, Long limit) {
        QArticleEntity articleEntity = QArticleEntity.articleEntity;

        return queryFactory
            .selectFrom(articleEntity)
            .where(articleEntity.boardId.eq(boardId))
            .orderBy(articleEntity.articleId.desc())
            .limit(limit)
            .fetch()
            .stream()
            .map(ArticleEntity::toArticle)
            .toList();
    }

    @Override
    public List<Article> findAllByBoardId(Long boardId, Long limit, Long lastArticleId) {
        QArticleEntity articleEntity = QArticleEntity.articleEntity;

        return queryFactory
            .selectFrom(articleEntity)
            .where(
                articleEntity.boardId.eq(boardId),
                articleEntity.articleId.lt(lastArticleId)
            )
            .orderBy(articleEntity.articleId.desc())
            .limit(limit)
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
    public void deleteById(Long articleId) {
        articleEntityRepository.deleteById(articleId);
    }

}
