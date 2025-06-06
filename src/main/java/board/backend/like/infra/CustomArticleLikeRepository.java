package board.backend.like.infra;

interface CustomArticleLikeRepository {

    boolean existsByArticleIdAndUserId(Long articleId, Long userId);

}
