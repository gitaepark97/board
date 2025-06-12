package board.backend.articleRead.web;

import board.backend.articleRead.application.ArticleReadService;
import board.backend.articleRead.web.response.ArticleSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
class ArticleReadController {

    private final ArticleReadService articleReadService;

    @GetMapping
    List<ArticleSummaryResponse> readAll(
        @RequestParam Long boardId,
        @RequestParam(required = false, defaultValue = "10") Long pageSize,
        @RequestParam(required = false) Long lastArticleId
    ) {
        return articleReadService.readAll(boardId, pageSize, lastArticleId)
            .stream()
            .map(ArticleSummaryResponse::from)
            .toList();
    }

}
