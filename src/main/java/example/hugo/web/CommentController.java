package example.hugo.web;

import example.hugo.application.CommentService;
import example.hugo.web.request.CommentCreateRequest;
import example.hugo.web.response.CommentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/comments")
class CommentController {

    private final CommentService commentService;

    @GetMapping("/{commentId}")
    CommentResponse read(@PathVariable Long commentId) {
        return CommentResponse.from(commentService.readComment(commentId));
    }

    @GetMapping
    public List<CommentResponse> readComments(
        @RequestParam Long articleId,
        @RequestParam(required = false, defaultValue = "10") Long pageSize,
        @RequestParam(required = false) Long lastParentCommentId,
        @RequestParam(required = false) Long lastCommentId
    ) {
        return commentService.readComments(articleId, pageSize, lastParentCommentId, lastCommentId)
            .stream()
            .map(CommentResponse::from)
            .toList();
    }

    @PostMapping
    CommentResponse createComment(@RequestBody @Valid CommentCreateRequest request) {
        return CommentResponse.from(commentService.createComment(request.articleId(), request.content(), request.parentCommentId(), request.writerId()));
    }

    @DeleteMapping("/{commentId}")
    void delete(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
    }

}
