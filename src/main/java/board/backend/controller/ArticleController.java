package board.backend.controller;

import board.backend.controller.request.ArticleCreateRequest;
import board.backend.controller.request.ArticleUpdateRequest;
import board.backend.domain.Article;
import board.backend.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    List<Article> readAll(
        @RequestParam Long boardId,
        @RequestParam(required = false, defaultValue = "10") Long pageSize,
        @RequestParam(required = false) Long lastArticleId
    ) {
        return articleService.readAll(boardId, pageSize, lastArticleId);
    }

    @GetMapping("/{articleId}")
    Article read(@PathVariable Long articleId) {
        return articleService.read(articleId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Article create(@RequestBody @Valid ArticleCreateRequest request) {
        return articleService.create(request.boardId(), request.writerId(), request.title(), request.content());
    }

    @PutMapping("/{articleId}")
    Article update(@PathVariable Long articleId, @RequestBody @Valid ArticleUpdateRequest request) {
        return articleService.update(articleId, request.title(), request.content());
    }

    @DeleteMapping("/{articleId}")
    void delete(@PathVariable Long articleId) {
        articleService.delete(articleId);
    }

}
