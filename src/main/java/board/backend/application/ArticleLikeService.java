package board.backend.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArticleLikeService {

    private final ArticleReader articleReader;
    private final ArticleLikeWriter articleLikeWriter;

    public void like(Long articleId, Long userId) {
        // 게시글 존재 확인
        articleReader.checkArticleExistOrThrow(articleId);

        articleLikeWriter.like(articleId, userId);
    }

    public void unlike(Long articleId, Long userId) {
        articleLikeWriter.unlike(articleId, userId);
    }

}
