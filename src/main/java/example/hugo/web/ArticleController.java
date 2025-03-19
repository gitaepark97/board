package example.hugo.web;

import example.hugo.application.ArticleService;
import example.hugo.web.request.ArticleCreateRequest;
import example.hugo.web.request.ArticleUpdateRequest;
import example.hugo.web.response.ArticleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/articles")
class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/{articleId}")
    ArticleResponse readArticle(@PathVariable Long articleId) {
        return ArticleResponse.from(articleService.readArticle(articleId));
    }

    @GetMapping
    List<ArticleResponse> readArticles(
        @RequestParam Long boardId,
        @RequestParam(required = false, defaultValue = "10") Long pageSize,
        @RequestParam(required = false) Long lastArticleId
    ) {
        return articleService.readArticles(boardId, pageSize, lastArticleId)
            .stream()
            .map(ArticleResponse::from)
            .toList();
    }

    @GetMapping("/boards/{boardId}/count")
    Long countBoardArticles(@PathVariable Long boardId) {
        return articleService.countBoardArticles(boardId);
    }

    @GetMapping("/{articleId}/view/count")
    Long countArticleViews(@PathVariable Long articleId) {
        return articleService.countArticleViews(articleId);
    }

    @PostMapping
    ArticleResponse createArticle(@RequestBody @Valid ArticleCreateRequest request) {
        return ArticleResponse.from(articleService.createArticle(request.title(), request.content(), request.boardId(), request.writerId()));
    }

    @PutMapping("/{articleId}")
    ArticleResponse updateArticle(@PathVariable Long articleId, @RequestBody @Valid ArticleUpdateRequest request) {
        return ArticleResponse.from(articleService.updateArticle(articleId, request.title(), request.content()));
    }

    @DeleteMapping("/{articleId}")
    void deleteArticle(@PathVariable Long articleId) {
        articleService.deleteArticle(articleId);
    }

}
