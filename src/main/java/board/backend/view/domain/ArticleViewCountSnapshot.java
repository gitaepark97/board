package board.backend.view.domain;

import board.backend.common.count.domain.ArticleCountSnapshot;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public class ArticleViewCountSnapshot extends ArticleCountSnapshot {

    static ArticleViewCountSnapshot create(LocalDate date, Long articleId, Long count) {
        return ArticleViewCountSnapshot.builder()
            .date(date)
            .articleId(articleId)
            .count(count)
            .build();
    }

}
