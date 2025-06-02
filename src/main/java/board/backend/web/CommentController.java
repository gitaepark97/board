package board.backend.web;

import board.backend.application.CommentService;
import board.backend.web.request.CommentCreateRequest;
import board.backend.web.response.CommentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
class CommentController {

    private final CommentService commentService;

    @GetMapping
    List<CommentResponse> readAll(
        @RequestParam Long articleId,
        @RequestParam(required = false, defaultValue = "10") Long pageSize,
        @RequestParam(required = false) Long lastParentCommentId,
        @RequestParam(required = false) Long lastCommentId
    ) {
        return commentService.readAll(articleId, pageSize, lastParentCommentId, lastCommentId)
            .stream()
            .map(CommentResponse::of)
            .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CommentResponse create(@RequestBody @Valid CommentCreateRequest request) {
        return CommentResponse.of(commentService.create(request.articleId(), request.writerId(), request.parentCommentId(), request.content()));
    }

    @DeleteMapping("/{commentId}")
    void delete(@PathVariable Long commentId) {
        commentService.delete(commentId);
    }

}
