package board.backend.like.infra.jpa;

import board.backend.common.infra.ArticleCountSnapshotId;
import board.backend.like.domain.ArticleLikeCountSnapshot;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@IdClass(ArticleCountSnapshotId.class)
@Entity
@Table(name = "article_like_count_snapshot")
public class ArticleLikeCountSnapshotEntity {

    @Id
    private LocalDate snapshotDate;

    @Id
    private Long articleId;

    private Long likeCount;

    public static ArticleLikeCountSnapshotEntity from(ArticleLikeCountSnapshot snapshot) {
        return new ArticleLikeCountSnapshotEntity(
            snapshot.date(),
            snapshot.articleId(),
            snapshot.likeCount()
        );
    }

    public ArticleLikeCountSnapshot toArticleViewCountSnapshot() {
        return ArticleLikeCountSnapshot.builder()
            .date(snapshotDate)
            .articleId(articleId)
            .likeCount(likeCount)
            .build();
    }

}
