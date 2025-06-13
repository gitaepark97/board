package board.backend.auth.infra.jpa;

import board.backend.auth.domain.LoginInfo;
import board.backend.auth.domain.LoginMethod;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "login_info")
class LoginInfoEntity {

    @Id
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private LoginMethod loginMethod;

    private String loginKey;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    static LoginInfoEntity from(LoginInfo loginInfo) {
        return new LoginInfoEntity(
            loginInfo.id(),
            loginInfo.userId(),
            loginInfo.loginMethod(),
            loginInfo.loginKey(),
            loginInfo.password(),
            loginInfo.createdAt(),
            loginInfo.updatedAt()
        );
    }
    
    LoginInfo toLoginInfo() {
        return LoginInfo.builder()
            .id(id)
            .userId(userId)
            .loginMethod(loginMethod)
            .loginKey(loginKey)
            .password(password)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
    }

}
