package board.backend.article.web;

import board.backend.article.application.ArticleService;
import board.backend.article.web.request.ArticleCreateRequest;
import board.backend.article.web.request.ArticleUpdateRequest;
import board.backend.article.web.response.ArticleResponse;
import board.backend.article.web.response.ArticleSummaryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    List<ArticleSummaryResponse> readAll(
        @RequestParam Long boardId,
        @RequestParam(required = false, defaultValue = "10") Long pageSize,
        @RequestParam(required = false) Long lastArticleId
    ) {
        return articleService.readAll(boardId, pageSize, lastArticleId)
            .stream()
            .map(ArticleSummaryResponse::from)
            .toList();
    }

    @GetMapping("/{articleId}")
    ArticleResponse read(@PathVariable Long articleId) {
        return ArticleResponse.from(articleService.read(articleId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ArticleResponse create(@AuthenticationPrincipal Long userId, @RequestBody @Valid ArticleCreateRequest request) {
        return ArticleResponse.from(articleService.create(request.boardId(), userId, request.title(), request.content()));
    }

    @PutMapping("/{articleId}")
    ArticleResponse update(
        @AuthenticationPrincipal Long userId,
        @PathVariable Long articleId,
        @RequestBody @Valid ArticleUpdateRequest request
    ) {
        return ArticleResponse.from(articleService.update(articleId, userId, request.title(), request.content()));
    }

    @DeleteMapping("/{articleId}")
    void delete(@AuthenticationPrincipal Long userId, @PathVariable Long articleId) {
        articleService.delete(articleId, userId);
    }

}
