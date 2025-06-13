package board.backend.view.infra.jpa;

import board.backend.view.domain.ArticleViewCount;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "article_view_count")
class ArticleViewCountEntity {

    @Id
    private Long articleId;

    private Long viewCount;

    static ArticleViewCountEntity from(ArticleViewCount articleViewCount) {
        return new ArticleViewCountEntity(
            articleViewCount.articleId(),
            articleViewCount.viewCount()
        );
    }

    ArticleViewCount toArticleViewCount() {
        return ArticleViewCount.builder()
            .articleId(articleId)
            .viewCount(viewCount)
            .build();
    }

}
