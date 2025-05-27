package board.backend.service;

import board.backend.domain.ArticleLike;
import board.backend.repository.ArticleLikeRepository;
import board.backend.support.TimeProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ArticleLikeService {

    private final TimeProvider timeProvider;
    private final ArticleLikeRepository articleLikeRepository;
    private final ArticleService articleService;

    @Transactional
    public void like(Long articleId, Long userId) {
        if (!articleLikeRepository.existsByArticleIdAndUserId(articleId, userId)) {
            // 게시글 존재 확인
            articleService.checkArticleExistOrThrow(articleId);

            // 게시글 좋아요 생성
            ArticleLike articleLike = ArticleLike.create(articleId, userId, timeProvider.now());
            // 게시글 좋아요 저장
            articleLikeRepository.save(articleLike);
        }
    }

    @Transactional
    public void unlike(Long articleId, Long userId) {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId).ifPresent(articleLikeRepository::delete);
    }

}
