package board.backend.comment.web;

import board.backend.comment.application.CommentService;
import board.backend.comment.web.request.CommentCreateRequest;
import board.backend.comment.web.response.CommentResponse;
import board.backend.comment.web.response.CommentWithWriterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
class CommentController {

    private final CommentService commentService;

    @GetMapping
    List<CommentWithWriterResponse> readAll(
        @RequestParam Long articleId,
        @RequestParam(required = false, defaultValue = "10") Long pageSize,
        @RequestParam(required = false) Long lastParentCommentId,
        @RequestParam(required = false) Long lastCommentId
    ) {
        return commentService.readAll(articleId, pageSize, lastParentCommentId, lastCommentId)
            .stream()
            .map(CommentWithWriterResponse::from)
            .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CommentResponse create(@AuthenticationPrincipal Long userId, @RequestBody @Valid CommentCreateRequest request) {

        return CommentResponse.from(commentService.create(request.articleIdAsLong(), userId, request.parentCommentIdAsLong(), request.content()));
    }

    @DeleteMapping("/{commentId}")
    void delete(@AuthenticationPrincipal Long userId, @PathVariable Long commentId) {
        commentService.delete(commentId, userId);
    }

}
