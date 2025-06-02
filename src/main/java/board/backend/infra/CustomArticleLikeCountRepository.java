package board.backend.infra;

interface CustomArticleLikeCountRepository {

    long increase(Long articleId);

    void decrease(Long articleId);

}
