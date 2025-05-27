package board.backend.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "article_like")
public class ArticleLike {

    @EmbeddedId
    private ArticleLikeId id;

    private LocalDateTime createdAt;

    public static ArticleLike create(Long articleId, Long userId, LocalDateTime now) {
        return ArticleLike.builder()
            .id(new ArticleLikeId(articleId, userId))
            .createdAt(now)
            .build();
    }

}
