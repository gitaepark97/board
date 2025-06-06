package board.backend.comment.application;

import board.backend.auth.application.dto.CommentWithWriter;
import board.backend.comment.domain.ArticleCommentCount;
import board.backend.comment.domain.Comment;
import board.backend.comment.infra.ArticleCommentCountRepository;
import board.backend.comment.infra.CommentRepository;
import board.backend.user.application.UserReader;
import board.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CommentReader {

    private final CommentRepository commentRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;
    private final UserReader userReader;

    public List<CommentWithWriter> readAll(
        Long articleId,
        Long pageSize,
        Long lastParentCommentId,
        Long lastCommentId
    ) {
        List<Comment> comments = lastParentCommentId == null || lastCommentId == null ?
            commentRepository.findAllById(articleId, pageSize) :
            commentRepository.findAllById(articleId, pageSize, lastParentCommentId, lastCommentId);

        List<Long> writerIds = comments.stream().map(Comment::getWriterId).toList();

        // 작성자 조회
        Map<Long, User> writerMap = userReader.readAll(writerIds);

        return comments.stream()
            .map(comment -> CommentWithWriter.of(comment, writerMap.get(comment.getWriterId())))
            .toList();
    }

    public Map<Long, Long> count(List<Long> articleIds) {
        return articleCommentCountRepository.findAllById(articleIds)
            .stream()
            .collect(Collectors.toMap(ArticleCommentCount::getArticleId, ArticleCommentCount::getCommentCount));
    }

}
