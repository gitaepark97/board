package example.hugo.infra.entity;

import example.hugo.domain.ArticleCommentCount;
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
@Entity(name = "article_comment_count")
@DynamicUpdate
public class ArticleCommentCountEntity {

    @Id
    private Long articleId;
    private Long commentCount;

    public static ArticleCommentCountEntity from(ArticleCommentCount articleCommentCount) {
        return ArticleCommentCountEntity.builder()
            .articleId(articleCommentCount.articleId())
            .commentCount(articleCommentCount.commentCount())
            .build();
    }

    public ArticleCommentCount toArticleCommentCount() {
        return new ArticleCommentCount(articleId, commentCount);
    }

}
