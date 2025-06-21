package board.backend.view.infra.jpa;

import board.backend.common.infra.ArticleCountSnapshotId;
import board.backend.view.domain.ArticleViewCountSnapshot;
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
@Table(name = "article_view_count_snapshot")
public class ArticleViewCountSnapshotEntity {

    @Id
    private LocalDate snapshotDate;

    @Id
    private Long articleId;

    private Long viewCount;

    public static ArticleViewCountSnapshotEntity from(ArticleViewCountSnapshot snapshot) {
        return new ArticleViewCountSnapshotEntity(
            snapshot.date(),
            snapshot.articleId(),
            snapshot.viewCount()
        );
    }

    public ArticleViewCountSnapshot toArticleViewCountSnapshot() {
        return ArticleViewCountSnapshot.builder()
            .date(snapshotDate)
            .articleId(articleId)
            .viewCount(viewCount)
            .build();
    }

}
