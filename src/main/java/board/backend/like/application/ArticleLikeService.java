package board.backend.like.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArticleLikeService {

    private final ArticleLikeWriter articleLikeWriter;

    public void like(Long articleId, Long userId) {
        articleLikeWriter.like(articleId, userId);
    }

    public void unlike(Long articleId, Long userId) {
        articleLikeWriter.unlike(articleId, userId);
    }

}
