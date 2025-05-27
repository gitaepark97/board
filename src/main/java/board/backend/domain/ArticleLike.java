package board.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@IdClass(ArticleLikeId.class)
@Entity
@Table(name = "article_like")
public class ArticleLike {

    @Id
    private Long articleId;

    @Id
    private Long userId;

    private LocalDateTime createdAt;

    public static ArticleLike create(Long articleId, Long userId, LocalDateTime now) {
        return ArticleLike.builder()
            .articleId(articleId)
            .userId(userId)
            .createdAt(now)
            .build();
    }

}
