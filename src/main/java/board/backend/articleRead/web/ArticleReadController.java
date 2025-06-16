package board.backend.articleRead.web;

import board.backend.articleRead.application.ArticleReadService;
import board.backend.articleRead.web.response.ArticleResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
class ArticleReadController {

    private final ArticleReadService articleReadService;

    @GetMapping
    List<ArticleResponse> readAll(
        @RequestParam Long boardId,
        @RequestParam(required = false, defaultValue = "10") Long pageSize,
        @RequestParam(required = false) Long lastArticleId
    ) {
        return articleReadService.readAll(boardId, pageSize, lastArticleId)
            .stream()
            .map(ArticleResponse::from)
            .toList();
    }

    @GetMapping("/{articleId}")
    ArticleResponse read(@PathVariable Long articleId, HttpServletRequest request) {
        return ArticleResponse.from(articleReadService.read(articleId, request.getRemoteAddr()));
    }

}
