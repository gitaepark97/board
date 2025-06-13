package board.backend.comment.infra.jpa;

import board.backend.comment.application.port.CommentRepository;
import board.backend.comment.domain.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
class CommentRepositoryImpl implements CommentRepository {

    private final JPAQueryFactory queryFactory;
    private final CommentEntityRepository commentEntityRepository;

    @Override
    public boolean existsById(Long id) {
        QCommentEntity commentEntity = QCommentEntity.commentEntity;

        Integer result = queryFactory
            .selectOne()
            .from(commentEntity)
            .where(commentEntity.id.eq(id))
            .fetchFirst();

        return result != null;
    }

    @Override
    public int countBy(Long articleId, Long parentId, Integer limit) {
        QCommentEntity commentEntity = QCommentEntity.commentEntity;

        List<Long> limitedComments = queryFactory
            .select(commentEntity.id)
            .from(commentEntity)
            .where(
                commentEntity.articleId.eq(articleId),
                commentEntity.parentId.eq(parentId)
            )
            .limit(limit)
            .fetch();

        return limitedComments.size();
    }

    @Override
    public Optional<Comment> findById(Long id) {
        return commentEntityRepository.findById(id).map(CommentEntity::toComment);
    }

    @Override
    public List<Comment> findAllById(Long articleId, Long pageSize) {
        QCommentEntity commentEntity = QCommentEntity.commentEntity;

        return queryFactory
            .selectFrom(commentEntity)
            .where(
                commentEntity.articleId.eq(articleId)
            )
            .orderBy(commentEntity.parentId.asc(), commentEntity.id.asc())
            .limit(pageSize)
            .fetch()
            .stream()
            .map(CommentEntity::toComment)
            .toList();
    }

    @Override
    public List<Comment> findAllById(
        Long articleId,
        Long pageSize,
        Long lastParentId,
        Long lastId
    ) {
        QCommentEntity commentEntity = QCommentEntity.commentEntity;

        return queryFactory
            .selectFrom(commentEntity)
            .where(
                commentEntity.articleId.eq(articleId),
                commentEntity.parentId.gt(lastParentId)
                    .or(commentEntity.parentId.eq(lastParentId).and(commentEntity.id.gt(lastId)))
            )
            .orderBy(commentEntity.parentId.asc(), commentEntity.id.asc())
            .limit(pageSize)
            .fetch()
            .stream()
            .map(CommentEntity::toComment)
            .toList();
    }

    @Override
    public void save(Comment comment) {
        commentEntityRepository.save(CommentEntity.from(comment));
    }

    @Override
    public void delete(Comment comment) {
        commentEntityRepository.delete(CommentEntity.from(comment));
    }

    @Override
    public void deleteByByArticleId(Long articleId) {
        commentEntityRepository.deleteByArticleId(articleId);
    }

}
