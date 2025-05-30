package board.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "article_comment_count")
public class ArticleCommentCount {

    @Id
    private Long articleId;

    private Long commentCount;

    public static ArticleCommentCount init(Long articleId) {
        return ArticleCommentCount.builder()
            .articleId(articleId)
            .commentCount(1L)
            .build();
    }

}
