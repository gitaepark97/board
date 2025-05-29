package board.backend.repository;

interface CustomCommentRepository {

    int countBy(Long articleId, Long parentId, Integer limit);

}
