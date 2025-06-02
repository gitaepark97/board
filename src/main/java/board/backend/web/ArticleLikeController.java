package board.backend.web;

import board.backend.application.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/article-likes")
class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @PostMapping("/articles/{articleId}/users/{userId}")
    void like(@PathVariable Long articleId, @PathVariable Long userId) {
        articleLikeService.like(articleId, userId);
    }

    @DeleteMapping("/articles/{articleId}/users/{userId}")
    void unlike(@PathVariable Long articleId, @PathVariable Long userId) {
        articleLikeService.unlike(articleId, userId);
    }

}
