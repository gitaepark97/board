package board.backend.controller;

import board.backend.service.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/article-likes")
class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

    @PostMapping("/articles/{articleId}/users/{userId}")
    void like(@PathVariable Long articleId, @PathVariable Long userId) {
        articleLikeService.like(articleId, userId);
    }

}
