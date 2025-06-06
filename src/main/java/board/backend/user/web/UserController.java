package board.backend.user.web;

import board.backend.user.application.UserService;
import board.backend.user.web.request.UserUpdateRequest;
import board.backend.user.web.response.UserResponse;
import board.backend.user.web.response.UserSummaryResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
class UserController {

    private final UserService userService;

    @GetMapping("/my")
    UserResponse readMy(@AuthenticationPrincipal Long userId) {
        return UserResponse.from(userService.read(userId));
    }

    @GetMapping("/{userId}")
    UserSummaryResponse read(@PathVariable Long userId) {
        return UserSummaryResponse.from(userService.read(userId));
    }

    @PutMapping("/my")
    UserResponse update(@AuthenticationPrincipal Long userId, @RequestBody @Valid UserUpdateRequest request) {
        return UserResponse.from(userService.update(userId, request.nickname()));
    }

}
