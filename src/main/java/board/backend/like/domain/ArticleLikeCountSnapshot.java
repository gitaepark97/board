package board.backend.like.domain;

import board.backend.common.count.domain.ArticleCountSnapshot;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public class ArticleLikeCountSnapshot extends ArticleCountSnapshot {

    static ArticleLikeCountSnapshot create(LocalDate date, Long articleId, Long count) {
        return ArticleLikeCountSnapshot.builder()
            .date(date)
            .articleId(articleId)
            .count(count)
            .build();
    }

}
