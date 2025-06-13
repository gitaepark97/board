package board.backend.auth.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LoginInfo(
    Long id,
    Long userId,
    LoginMethod loginMethod,
    String loginKey,
    String password,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static LoginInfo create(Long id, String email, String password, Long userId, LocalDateTime now) {
        return LoginInfo.builder()
            .id(id)
            .userId(userId)
            .loginMethod(LoginMethod.EMAIL)
            .loginKey(email)
            .password(password)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

}
