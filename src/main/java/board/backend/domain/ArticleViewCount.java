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
@Table(name = "article_view_count")
public class ArticleViewCount {

    @Id
    private Long articleId;

    private Long viewCount;

    public static ArticleViewCount init(Long articleId) {
        return ArticleViewCount.builder()
            .articleId(articleId)
            .viewCount(1L)
            .build();
    }

}
