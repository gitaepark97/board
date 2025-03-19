package example.hugo.infra.entity;


import example.hugo.domain.ArticleLike;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "article_like")
@DynamicUpdate
public class ArticleLikeEntity {

    @Id
    private Long articleLikeId;
    private Long articleId;
    private Long userId;
    private LocalDateTime createdAt;

    public static ArticleLikeEntity from(ArticleLike articleLike) {
        return ArticleLikeEntity.builder()
            .articleLikeId(articleLike.articleLikeId())
            .articleId(articleLike.articleId())
            .userId(articleLike.userId())
            .createdAt(articleLike.createdAt())
            .build();
    }

    public ArticleLike toArticleLike() {
        return ArticleLike.builder()
            .articleLikeId(articleLikeId)
            .articleId(articleId)
            .userId(userId)
            .createdAt(createdAt)
            .build();
    }

}
