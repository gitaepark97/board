package board.backend.controller;

import board.backend.application.CommentService;
import board.backend.controller.request.CommentCreateRequest;
import board.backend.controller.response.CommentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
class CommentController {

    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CommentResponse create(@RequestBody @Valid CommentCreateRequest request) {
        return CommentResponse.of(commentService.create(request.articleId(), request.writerId(), request.parentCommentId(), request.content()));
    }

}
