package example.hugo.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import example.hugo.application.ArticleCommentCountRepository;
import example.hugo.domain.ArticleCommentCount;
import example.hugo.infra.entity.ArticleCommentCountEntity;
import example.hugo.infra.entity.ArticleCommentCountEntityRepository;
import example.hugo.infra.entity.QArticleCommentCountEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
class ArticleCommentCountRepositoryImpl implements ArticleCommentCountRepository {

    private final ArticleCommentCountEntityRepository articleCommentCountEntityRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<ArticleCommentCount> findByArticleId(Long articleId) {
        return articleCommentCountEntityRepository.findById(articleId)
            .map(ArticleCommentCountEntity::toArticleCommentCount);
    }

    @Override
    public void save(ArticleCommentCount articleCommentCount) {
        articleCommentCountEntityRepository.save(ArticleCommentCountEntity.from(articleCommentCount));
    }

    @Override
    public long increase(Long articleId) {
        QArticleCommentCountEntity articleCommentCountEntity = QArticleCommentCountEntity.articleCommentCountEntity;

        return queryFactory
            .update(articleCommentCountEntity)
            .set(articleCommentCountEntity.commentCount, articleCommentCountEntity.commentCount.add(1))
            .where(articleCommentCountEntity.articleId.eq(articleId))
            .execute();
    }

    @Override
    public void decrease(Long articleId) {
        QArticleCommentCountEntity articleCommentCountEntity = QArticleCommentCountEntity.articleCommentCountEntity;

        queryFactory
            .update(articleCommentCountEntity)
            .set(articleCommentCountEntity.commentCount, articleCommentCountEntity.commentCount.subtract(1))
            .where(articleCommentCountEntity.articleId.eq(articleId))
            .execute();
    }

}
