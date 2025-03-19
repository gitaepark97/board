package example.hugo.web;

import example.hugo.application.ArticleLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/article-likes")
class ArticleLikeController {

    private final ArticleLikeService articleLikeService;

}
