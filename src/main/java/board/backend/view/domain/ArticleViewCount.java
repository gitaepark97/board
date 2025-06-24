package board.backend.view.domain;

import board.backend.common.count.domain.ArticleCount;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public class ArticleViewCount extends ArticleCount {

    public static ArticleViewCount init(Long articleId) {
        return ArticleViewCount.builder()
            .articleId(articleId)
            .count(0L)
            .build();
    }

    public static ArticleViewCount create(Long articleId, Long count) {
        return ArticleViewCount.builder()
            .articleId(articleId)
            .count(count)
            .build();
    }

    public ArticleViewCountSnapshot toSnapshot(LocalDate date) {
        return count != 0L ? ArticleViewCountSnapshot.create(date, articleId, count) : null;
    }

}
