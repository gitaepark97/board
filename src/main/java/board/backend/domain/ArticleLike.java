package board.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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

    @Id
    private Long id;

    private Long articleId;

    private Long userId;

    private LocalDateTime createdAt;

    public static ArticleLike like(Long id, Long articleId, Long userId, LocalDateTime now) {
        return ArticleLike.builder()
            .id(id)
            .articleId(articleId)
            .userId(userId)
            .createdAt(now)
            .build();
    }

}
