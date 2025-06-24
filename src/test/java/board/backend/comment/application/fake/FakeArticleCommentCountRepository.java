package board.backend.comment.application.fake;

import board.backend.comment.application.port.ArticleCommentCountRepository;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.common.count.application.fake.FakeArticleCountRepository;

public class FakeArticleCommentCountRepository extends FakeArticleCountRepository<ArticleCommentCount> implements ArticleCommentCountRepository {

    @Override
    public void increase(Long articleId) {
        ArticleCommentCount existing = store.get(articleId);

        if (existing == null) {
            store.put(articleId, ArticleCommentCount.builder().articleId(articleId).count(1L).build());
        } else {
            store.put(articleId, ArticleCommentCount.builder()
                .articleId(articleId)
                .count(existing.getCount() + 1)
                .build());
        }
    }

    @Override
    public void decrease(Long articleId) {
        ArticleCommentCount existing = store.get(articleId);
        if (existing != null) {
            store.put(articleId, ArticleCommentCount.builder()
                .articleId(articleId)
                .count(existing.getCount() - 1)
                .build());
        }
    }

}