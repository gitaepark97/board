package board.backend.comment.domain;

import board.backend.common.count.domain.ArticleCountSnapshot;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public class ArticleCommentCountSnapshot extends ArticleCountSnapshot {

    static ArticleCommentCountSnapshot create(LocalDate date, Long articleId, Long count) {
        return ArticleCommentCountSnapshot.builder()
            .date(date)
            .articleId(articleId)
            .count(count)
            .build();
    }

}
