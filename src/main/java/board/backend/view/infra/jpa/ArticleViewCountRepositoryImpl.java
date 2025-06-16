package board.backend.view.infra.jpa;

import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class ArticleViewCountRepositoryImpl implements ArticleViewCountRepository {

    private final JPAQueryFactory queryFactory;
    private final ArticleViewCountEntityRepository articleViewCountEntityRepository;

    @Override
    public Optional<ArticleViewCount> findById(Long articleId) {
        return articleViewCountEntityRepository.findById(articleId).map(ArticleViewCountEntity::toArticleViewCount);
    }

    @Override
    public List<ArticleViewCount> findAllById(List<Long> articleIds) {
        return articleViewCountEntityRepository.findAllById(articleIds)
            .stream()
            .map(ArticleViewCountEntity::toArticleViewCount)
            .toList();
    }

    @Override
    public void save(ArticleViewCount articleViewCount) {
        articleViewCountEntityRepository.save(ArticleViewCountEntity.from(articleViewCount));
    }

    @Override
    public void deleteById(Long articleId) {
        articleViewCountEntityRepository.deleteById(articleId);
    }

    @Override
    public void increase(Long articleId) {
        QArticleViewCountEntity articleViewCountEntity = QArticleViewCountEntity.articleViewCountEntity;

        queryFactory.update(articleViewCountEntity)
            .set(articleViewCountEntity.viewCount, articleViewCountEntity.viewCount.add(1))
            .where(articleViewCountEntity.articleId.eq(articleId))
            .execute();
    }

}
