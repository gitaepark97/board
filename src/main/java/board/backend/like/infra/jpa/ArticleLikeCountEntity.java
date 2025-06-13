package board.backend.like.infra.jpa;

import board.backend.like.domain.ArticleLikeCount;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "article_like_count")
class ArticleLikeCountEntity {

    @Id
    private Long articleId;

    private Long likeCount;

    static ArticleLikeCountEntity from(ArticleLikeCount articleLikeCount) {
        return new ArticleLikeCountEntity(
            articleLikeCount.articleId(),
            articleLikeCount.likeCount()
        );
    }

    ArticleLikeCount toArticleLikeCount() {
        return ArticleLikeCount.builder()
            .articleId(articleId)
            .likeCount(likeCount)
            .build();
    }

}
