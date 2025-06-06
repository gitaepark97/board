package board.backend.auth.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "login_info")
public class LoginInfo {

    @Id
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private LoginMethod loginMethod;

    private String loginKey;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

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
