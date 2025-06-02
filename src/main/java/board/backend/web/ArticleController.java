package board.backend.web;

import board.backend.application.ArticleService;
import board.backend.web.request.ArticleCreateRequest;
import board.backend.web.request.ArticleUpdateRequest;
import board.backend.web.response.ArticleResponse;
import board.backend.web.response.ArticleSummaryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
            .map(ArticleSummaryResponse::of)
            .toList();
    }

    @GetMapping("/{articleId}")
    ArticleResponse read(@PathVariable Long articleId) {
        return ArticleResponse.of(articleService.read(articleId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ArticleResponse create(@RequestBody @Valid ArticleCreateRequest request) {
        return ArticleResponse.of(articleService.create(request.boardId(), request.writerId(), request.title(), request.content()));
    }

    @PutMapping("/{articleId}")
    ArticleResponse update(@PathVariable Long articleId, @RequestBody @Valid ArticleUpdateRequest request) {
        return ArticleResponse.of(articleService.update(articleId, request.title(), request.content()));
    }

    @DeleteMapping("/{articleId}")
    void delete(@PathVariable Long articleId) {
        articleService.delete(articleId);
    }

}
