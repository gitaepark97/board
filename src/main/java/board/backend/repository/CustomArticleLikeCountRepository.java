package board.backend.repository;

interface CustomArticleLikeCountRepository {

    long increase(Long articleId);

    void decrease(Long articleId);

}
