package example.hugo.application;

import example.hugo.domain.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentReader commentReader;
    private final CommentWriter commentWriter;
    private final ArticleReader articleReader;

    public Comment readComment(Long commentId) {
        return commentReader.readComment(commentId);
    }

    public List<Comment> readComments(Long articleId, Long pageSize, Long lastParentCommentId, Long lastCommentId) {
        return commentReader.readComments(articleId, pageSize, lastParentCommentId, lastCommentId);
    }

    public Long countArticleComment(Long articleId) {
        return commentReader.countArticleComments(articleId);
    }

    public Comment createComment(Long articleId, String content, Long parentCommentId, Long writerId) {
        articleReader.checkArticleExists(articleId);
        return commentWriter.createComment(articleId, content, parentCommentId, writerId);
    }

    public void deleteComment(Long commentId) {
        commentWriter.deleteComment(commentId);
    }

}
