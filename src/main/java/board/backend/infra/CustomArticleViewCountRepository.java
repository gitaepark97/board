package board.backend.infra;

interface CustomArticleViewCountRepository {

    long increase(Long articleId);
    
}
