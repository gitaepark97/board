package board.backend.comment.infra;

interface CustomArticleCommentCountRepository {
    
    void decrease(Long articleId);

}
