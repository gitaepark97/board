package board.backend.user.web;

import board.backend.user.application.UserService;
import board.backend.user.web.request.UserUpdateRequest;
import board.backend.user.web.response.MyUserResponse;
import board.backend.user.web.response.UserResponse;
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
    MyUserResponse readMy(@AuthenticationPrincipal Long userId) {
        return MyUserResponse.from(userService.read(userId));
    }

    @GetMapping("/{userId}")
    UserResponse read(@PathVariable Long userId) {
        return UserResponse.from(userService.read(userId));
    }

    @PutMapping("/my")
    MyUserResponse update(@AuthenticationPrincipal Long userId, @RequestBody @Valid UserUpdateRequest request) {
        return MyUserResponse.from(userService.update(userId, request.nickname()));
    }

}
