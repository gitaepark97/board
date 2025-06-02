package board.backend.repository;

interface CustomArticleLikeRepository {

    boolean existsByArticleIdAndUserId(Long articleId, Long userId);

}
