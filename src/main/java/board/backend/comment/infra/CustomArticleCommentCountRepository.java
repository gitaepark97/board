package board.backend.comment.infra;

interface CustomArticleCommentCountRepository {

    long increase(Long articleId);

    void decrease(Long articleId);

}
