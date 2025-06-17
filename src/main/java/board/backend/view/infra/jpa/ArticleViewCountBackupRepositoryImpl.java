package board.backend.view.infra.jpa;

import board.backend.view.application.port.ArticleViewCountBackupRepository;
import board.backend.view.domain.ArticleViewCount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class ArticleViewCountBackupRepositoryImpl implements ArticleViewCountBackupRepository {

    private final ArticleViewCountBackupEntityRepository articleViewCountEntityRepository;

    @Override
    public Optional<ArticleViewCount> findById(Long articleId) {
        return articleViewCountEntityRepository.findById(articleId)
            .map(ArticleViewCountBackupEntity::toArticleViewCount);
    }

    @Override
    public List<ArticleViewCount> findAllById(List<Long> articleIds) {
        return articleViewCountEntityRepository.findAllById(articleIds)
            .stream()
            .map(ArticleViewCountBackupEntity::toArticleViewCount)
            .toList();
    }

    @Override
    public void save(ArticleViewCount articleViewCount) {
        articleViewCountEntityRepository.upsert(articleViewCount.articleId(), articleViewCount.viewCount());
    }

    @Override
    public void deleteById(Long articleId) {
        articleViewCountEntityRepository.deleteById(articleId);
    }

}
