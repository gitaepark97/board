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

    @Transactional
    public void like(Long articleId, Long userId) {
        ArticleLike articleLike = ArticleLike.create(articleId, userId, timeProvider.now());
        articleLikeRepository.save(articleLike);
    }

    @Transactional
    public void unlike(Long articleId, Long userId) {
        articleLikeRepository.findByArticleIdAndUserId(articleId, userId).ifPresent(articleLikeRepository::delete);
    }

}
