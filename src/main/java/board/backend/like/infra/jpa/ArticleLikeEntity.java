package board.backend.like.infra.jpa;

import board.backend.like.domain.ArticleLike;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@IdClass(ArticleLikeId.class)
@Entity
@Table(name = "article_like")
class ArticleLikeEntity {

    @Id
    private Long articleId;

    @Id
    private Long userId;

    private LocalDateTime createdAt;

    static ArticleLikeEntity from(ArticleLike articleLike) {
        return new ArticleLikeEntity(
            articleLike.articleId(),
            articleLike.userId(),
            articleLike.createdAt()
        );
    }

    ArticleLike toArticleLike() {
        return ArticleLike.builder()
            .articleId(articleId)
            .userId(userId)
            .createdAt(createdAt)
            .build();
    }

}
