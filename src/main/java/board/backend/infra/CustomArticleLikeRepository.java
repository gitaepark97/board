package board.backend.infra;

interface CustomArticleLikeRepository {

    boolean existsByArticleIdAndUserId(Long articleId, Long userId);

}
