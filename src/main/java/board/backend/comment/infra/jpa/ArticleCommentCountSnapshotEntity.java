package board.backend.comment.infra.jpa;

import board.backend.comment.domain.ArticleCommentCountSnapshot;
import board.backend.common.infra.ArticleCountSnapshotId;
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
@Table(name = "article_comment_count_snapshot")
public class ArticleCommentCountSnapshotEntity {

    @Id
    private LocalDate snapshotDate;

    @Id
    private Long articleId;

    private Long commentCount;

    public static ArticleCommentCountSnapshotEntity from(ArticleCommentCountSnapshot snapshot) {
        return new ArticleCommentCountSnapshotEntity(
            snapshot.date(),
            snapshot.articleId(),
            snapshot.commentCount()
        );
    }

    public ArticleCommentCountSnapshot toArticleViewCountSnapshot() {
        return ArticleCommentCountSnapshot.builder()
            .date(snapshotDate)
            .articleId(articleId)
            .commentCount(commentCount)
            .build();
    }

}
