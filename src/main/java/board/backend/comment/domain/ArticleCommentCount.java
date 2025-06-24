package board.backend.comment.domain;

import board.backend.common.count.domain.ArticleCount;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleCommentCount extends ArticleCount {

    public static ArticleCommentCount init(Long articleId) {
        return ArticleCommentCount.builder()
            .articleId(articleId)
            .count(1L)
            .build();
    }

    public ArticleCommentCountSnapshot toSnapshot(LocalDate date) {
        return count != 0L ? ArticleCommentCountSnapshot.create(date, articleId, count) : null;
    }

}
