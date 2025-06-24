package board.backend.comment.infra.jpa;

import board.backend.comment.domain.ArticleCommentCount;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "article_comment_count")
public class ArticleCommentCountEntity {

    @Id
    private Long articleId;

    private Long commentCount;

    static ArticleCommentCountEntity from(ArticleCommentCount articleCommentCount) {
        return new ArticleCommentCountEntity(
            articleCommentCount.getArticleId(),
            articleCommentCount.getCount()
        );
    }

    ArticleCommentCount toArticleCommentCount() {
        return ArticleCommentCount.builder()
            .articleId(articleId)
            .count(commentCount)
            .build();
    }

}
