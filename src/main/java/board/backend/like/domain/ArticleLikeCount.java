package board.backend.like.domain;

import board.backend.common.count.domain.ArticleCount;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleLikeCount extends ArticleCount {

    public static ArticleLikeCount init(Long articleId) {
        return ArticleLikeCount.builder()
            .articleId(articleId)
            .count(1L)
            .build();
    }

    public ArticleLikeCountSnapshot toSnapshot(LocalDate date) {
        return count != 0L ? ArticleLikeCountSnapshot.create(date, articleId, count) : null;
    }

}
