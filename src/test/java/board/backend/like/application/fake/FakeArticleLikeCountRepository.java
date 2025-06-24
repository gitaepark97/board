package board.backend.like.application.fake;

import board.backend.common.count.application.fake.FakeArticleCountRepository;
import board.backend.like.application.port.ArticleLikeCountRepository;
import board.backend.like.domain.ArticleLikeCount;

public class FakeArticleLikeCountRepository extends FakeArticleCountRepository<ArticleLikeCount> implements ArticleLikeCountRepository {

    @Override
    public void increase(Long articleId) {
        ArticleLikeCount existing = store.get(articleId);

        if (existing == null) {
            store.put(articleId, ArticleLikeCount.builder().articleId(articleId).count(1L).build());
        } else {
            store.put(articleId, ArticleLikeCount.builder()
                .articleId(articleId)
                .count(existing.getCount() + 1)
                .build());
        }
    }

    @Override
    public void decrease(Long articleId) {
        ArticleLikeCount existing = store.get(articleId);

        if (existing != null && existing.getCount() > 0) {
            store.put(articleId, ArticleLikeCount.builder()
                .articleId(articleId)
                .count(existing.getCount() - 1)
                .build());
        }
    }

    public void save(ArticleLikeCount articleLikeCount) {
        store.put(articleLikeCount.getArticleId(), articleLikeCount);
    }

}
