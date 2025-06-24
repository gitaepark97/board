package board.backend.common.count.domain;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@SuperBuilder
public abstract class ArticleCountSnapshot {

    protected LocalDate date;
    protected Long articleId;
    protected Long count;

}
