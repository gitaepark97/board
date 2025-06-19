package board.backend.like.application;

import board.backend.like.application.port.ArticleLikeCountRepository;
import board.backend.like.application.port.ArticleLikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleLikeDeleter {

    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleLikeCountRepository articleLikeCountRepository;

    @Transactional
    void deleteArticle(Long articleId) {
        // 게시글 좋아요 삭제
        articleLikeRepository.deleteByArticleId(articleId);

        // 게시글 좋아요 수 삭제
        articleLikeCountRepository.deleteById(articleId);
    }

}
