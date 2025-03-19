package example.hugo.infra.entity;

import example.hugo.domain.ArticleViewCount;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "article_view_count")
@DynamicUpdate
public class ArticleViewCountEntity {

    @Id
    private Long articleId;
    private Long viewCount;

    public static ArticleViewCountEntity from(ArticleViewCount articleCommentCount) {
        return ArticleViewCountEntity.builder()
            .articleId(articleCommentCount.articleId())
            .viewCount(articleCommentCount.viewCount())
            .build();
    }

    public ArticleViewCount toArticleViewCount() {
        return new ArticleViewCount(articleId, viewCount);
    }

}
