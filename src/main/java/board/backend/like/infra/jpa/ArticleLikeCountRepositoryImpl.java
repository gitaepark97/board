package board.backend.like.infra.jpa;

import board.backend.like.application.port.ArticleLikeCountRepository;
import board.backend.like.domain.ArticleLikeCount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class ArticleLikeCountRepositoryImpl implements ArticleLikeCountRepository {

    private final JPAQueryFactory queryFactory;
    private final ArticleLikeCountEntityRepository articleLikeCountEntityRepository;

    @Override
    public Optional<ArticleLikeCount> findById(Long articleId) {
        return articleLikeCountEntityRepository.findById(articleId).map(ArticleLikeCountEntity::toArticleLikeCount);
    }

    @Override
    public List<ArticleLikeCount> findAllById(List<Long> articleIds) {
        return articleLikeCountEntityRepository.findAllById(articleIds)
            .stream()
            .map(ArticleLikeCountEntity::toArticleLikeCount)
            .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long articleId) {
        articleLikeCountEntityRepository.deleteById(articleId);
    }

    @Override
    public void increaseOrSave(Long articleId, Long likeCount) {
        articleLikeCountEntityRepository.increaseOrSave(articleId, likeCount);
    }

    @Override
    public void decrease(Long articleId) {
        QArticleLikeCountEntity articleLikeCountEntity = QArticleLikeCountEntity.articleLikeCountEntity;

        queryFactory.update(articleLikeCountEntity)
            .set(articleLikeCountEntity.likeCount, articleLikeCountEntity.likeCount.subtract(1))
            .where(articleLikeCountEntity.articleId.eq(articleId))
            .execute();
    }

}
