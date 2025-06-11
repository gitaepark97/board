package board.backend.like.infra;

interface CustomArticleLikeCountRepository {

    void decrease(Long articleId);

}
