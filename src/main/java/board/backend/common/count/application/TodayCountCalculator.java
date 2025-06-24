package board.backend.common.count.application;

import board.backend.common.count.domain.ArticleCount;

public interface TodayCountCalculator {

    long calculate(ArticleCount articleCount);

}
