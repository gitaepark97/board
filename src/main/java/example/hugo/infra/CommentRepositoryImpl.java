package example.hugo.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import example.hugo.application.CommentRepository;
import example.hugo.domain.Comment;
import example.hugo.infra.entity.CommentEntity;
import example.hugo.infra.entity.CommentEntityRepository;
import example.hugo.infra.entity.QCommentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
class CommentRepositoryImpl implements CommentRepository {

    private final CommentEntityRepository commentEntityRepository;
    private final JPAQueryFactory queryFactory;

    @Override
    public Long countByParentCommentId(Long parentCommentId, Long limit) {
        QCommentEntity commentEntity = QCommentEntity.commentEntity;

        return queryFactory
            .select(commentEntity.count())
            .from(commentEntity)
            .where(commentEntity.parentCommentId.eq(parentCommentId))
            .orderBy(commentEntity.parentCommentId.asc(), commentEntity.commentId.asc())
            .limit(limit)
            .fetchOne();
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return commentEntityRepository.findById(commentId).map(CommentEntity::toComment);
    }

    @Override
    public List<Comment> findAllByArticleId(Long articleId, Long limit) {
        QCommentEntity commentEntity = QCommentEntity.commentEntity;

        return queryFactory
            .selectFrom(commentEntity)
            .where(commentEntity.articleId.eq(articleId))
            .orderBy(commentEntity.parentCommentId.asc(), commentEntity.commentId.asc())
            .limit(limit)
            .fetch()
            .stream()
            .map(CommentEntity::toComment)
            .toList();
    }

    @Override
    public List<Comment> findAllByArticleId(Long articleId, Long limit, Long lastParentCommentId, Long lastCommentId) {
        QCommentEntity commentEntity = QCommentEntity.commentEntity;

        return queryFactory
            .selectFrom(commentEntity)
            .where(
                commentEntity.articleId.eq(articleId),
                commentEntity.parentCommentId.gt(lastParentCommentId)
                    .or(commentEntity.parentCommentId.eq(lastParentCommentId)
                        .and(commentEntity.commentId.gt(lastCommentId)))
            )
            .orderBy(commentEntity.parentCommentId.desc(), commentEntity.commentId.desc())
            .limit(limit)
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

}
