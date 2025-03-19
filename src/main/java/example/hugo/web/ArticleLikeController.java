package example.hugo.web;

import example.hugo.application.ArticleLikeService;
import example.hugo.web.response.ArticleLikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/article-likes")
class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @GetMapping("/articles/{articleId}/users/{userId}")
    ArticleLikeResponse read(@PathVariable Long articleId, @PathVariable Long userId) {
        return ArticleLikeResponse.from(articleLikeService.readArticleLike(articleId, userId));
    }

    @GetMapping("/articles/{articleId}/count")
    Long count(@PathVariable Long articleId) {
        return articleLikeService.countArticleLikes(articleId);
    }

    @PostMapping("/articles/{articleId}/users/{userId}")
    void like(@PathVariable Long articleId, @PathVariable Long userId) {
        articleLikeService.like(articleId, userId);
    }

    @DeleteMapping("/articles/{articleId}/users/{userId}")
    void unlike(@PathVariable Long articleId, @PathVariable Long userId) {
        articleLikeService.unlike(articleId, userId);
    }

}
