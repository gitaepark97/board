package board.backend.comment.infra.jpa;

import board.backend.comment.application.port.ArticleCommentCountRepository;
import board.backend.comment.domain.ArticleCommentCount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class ArticleCommentCountRepositoryImpl implements ArticleCommentCountRepository {

    private final JPAQueryFactory queryFactory;
    private final ArticleCommentCountEntityRepository articleCommentCountEntityRepository;

    @Override
    public List<ArticleCommentCount> findAllById(List<Long> articleIds) {
        return articleCommentCountEntityRepository.findAllById(articleIds)
            .stream()
            .map(ArticleCommentCountEntity::toArticleCommentCount)
            .toList();
    }

    @Override
    public void deleteById(Long articleId) {
        articleCommentCountEntityRepository.deleteById(articleId);
    }

    @Override
    public void increaseOrSave(Long articleId, Long commentCount) {
        articleCommentCountEntityRepository.increaseOrSave(articleId, commentCount);
    }

    @Override
    public void decrease(Long articleId) {
        QArticleCommentCountEntity articleCommentCountEntity = QArticleCommentCountEntity.articleCommentCountEntity;

        queryFactory.update(articleCommentCountEntity)
            .set(articleCommentCountEntity.commentCount, articleCommentCountEntity.commentCount.subtract(1))
            .where(articleCommentCountEntity.articleId.eq(articleId))
            .execute();
    }

}
