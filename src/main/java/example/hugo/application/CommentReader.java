package example.hugo.application;

import example.hugo.domain.Comment;
import example.hugo.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class CommentReader {

    private final CommentRepository commentRepository;

    Comment readComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(ErrorCode.NOT_FOUND_COMMENT::toException);
    }

    List<Comment> readComments(Long articleId, Long pageSize, Long lastParentCommentId, Long lastCommentId) {
        return lastParentCommentId == null || lastCommentId == null ?
            commentRepository.findAllByArticleId(articleId, pageSize) :
            commentRepository.findAllByArticleId(articleId, pageSize, lastParentCommentId, lastCommentId);

    }

}
