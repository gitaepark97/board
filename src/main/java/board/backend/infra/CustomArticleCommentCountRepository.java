package board.backend.infra;

interface CustomArticleCommentCountRepository {

    long increase(Long articleId);

    void decrease(Long articleId);

}
