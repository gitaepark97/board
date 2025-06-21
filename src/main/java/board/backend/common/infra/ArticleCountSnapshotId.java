package board.backend.common.infra;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;

@Embeddable
public record ArticleCountSnapshotId(
    LocalDate snapshotDate,
    Long articleId
) implements Serializable {

}
