package board.backend.like.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "article_like_count")
public class ArticleLikeCount {

    @Id
    Long articleId;

    private Long likeCount;

    public static ArticleLikeCount init(Long articleId) {
        return ArticleLikeCount.builder()
            .articleId(articleId)
            .likeCount(1L)
            .build();
    }

}
