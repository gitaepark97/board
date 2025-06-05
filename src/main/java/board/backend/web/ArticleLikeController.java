package board.backend.web;

import board.backend.application.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/article-likes")
class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @PostMapping("/articles/{articleId}")
    void like(@AuthenticationPrincipal Long userId, @PathVariable Long articleId) {
        articleLikeService.like(articleId, userId);
    }

    @DeleteMapping("/articles/{articleId}")
    void unlike(@AuthenticationPrincipal Long userId, @PathVariable Long articleId) {
        articleLikeService.unlike(articleId, userId);
    }

}
