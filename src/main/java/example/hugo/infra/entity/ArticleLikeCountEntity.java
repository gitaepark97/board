package example.hugo.infra.entity;

import example.hugo.domain.ArticleLikeCount;
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
@Entity(name = "article_like_count")
@DynamicUpdate
public class ArticleLikeCountEntity {

    @Id
    private Long articleId;
    private Long likeCount;

    public static ArticleLikeCountEntity from(ArticleLikeCount articleLikeCount) {
        return ArticleLikeCountEntity.builder()
            .articleId(articleLikeCount.articleId())
            .likeCount(articleLikeCount.likeCount())
            .build();
    }

    public ArticleLikeCount toArticleLikeCount() {
        return new ArticleLikeCount(articleId, likeCount);
    }

}
