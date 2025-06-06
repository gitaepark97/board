package board.backend.application;

import board.backend.domain.ArticleCommentCount;
import board.backend.domain.Comment;
import board.backend.infra.ArticleCommentCountRepository;
import board.backend.infra.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
class CommentReader {

    private final CommentRepository commentRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;

    List<Comment> readAll(Long articleId, Long pageSize, Long lastParentCommentId, Long lastCommentId) {
        return lastParentCommentId == null || lastCommentId == null ?
            commentRepository.findAllById(articleId, pageSize) :
            commentRepository.findAllById(articleId, pageSize, lastParentCommentId, lastCommentId);
    }

    Map<Long, Long> count(List<Long> articleIds) {
        return articleCommentCountRepository.findAllById(articleIds)
            .stream()
            .collect(Collectors.toMap(ArticleCommentCount::getArticleId, ArticleCommentCount::getCommentCount));
    }

}
