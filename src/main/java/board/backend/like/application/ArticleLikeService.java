package board.backend.like.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArticleLikeService {

    private final ArticleLikeManager articleLikeManager;

    public void like(Long articleId, Long userId) {
        articleLikeManager.like(articleId, userId);
    }

    public void unlike(Long articleId, Long userId) {
        articleLikeManager.unlike(articleId, userId);
    }

}
