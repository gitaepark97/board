package board.backend.application;

import board.backend.domain.Comment;
import board.backend.infra.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
class CommentReader {

    private final CommentRepository commentRepository;

    List<Comment> readAll(Long articleId, Long pageSize, Long lastParentCommentId, Long lastCommentId) {
        return lastParentCommentId == null || lastCommentId == null ?
            commentRepository.findAllByArticleId(articleId, pageSize) :
            commentRepository.findAllByArticleId(articleId, pageSize, lastParentCommentId, lastCommentId);
    }

}
