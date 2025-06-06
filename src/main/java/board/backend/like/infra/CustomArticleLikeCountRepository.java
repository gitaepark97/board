package board.backend.like.infra;

interface CustomArticleLikeCountRepository {

    long increase(Long articleId);

    void decrease(Long articleId);

}
