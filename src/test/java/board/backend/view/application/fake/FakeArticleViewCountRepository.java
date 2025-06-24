package board.backend.view.application.fake;

import board.backend.common.count.application.fake.FakeArticleCountRepository;
import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;

public class FakeArticleViewCountRepository extends FakeArticleCountRepository<ArticleViewCount> implements ArticleViewCountRepository {

    @Override
    public Long increase(Long articleId) {
        ArticleViewCount existing = store.get(articleId);

        if (existing == null) {
            store.put(articleId, ArticleViewCount.builder().articleId(articleId).count(1L).build());
        } else {
            store.put(articleId, ArticleViewCount.builder()
                .articleId(articleId)
                .count(existing.getCount() + 1)
                .build());
        }

        return store.get(articleId).getCount();
    }

}
