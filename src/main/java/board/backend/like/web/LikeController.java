package board.backend.like.web;

import board.backend.like.application.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/likes")
class LikeController {

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
