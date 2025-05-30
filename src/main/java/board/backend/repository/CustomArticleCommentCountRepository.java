package board.backend.repository;

interface CustomArticleCommentCountRepository {

    long increase(Long articleId);

    void decrease(Long articleId);

}
