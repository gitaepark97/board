package board.backend.user.infra.jpa;

import board.backend.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
class UserEntity {

    @Id
    private Long id;

    private String email;

    private String nickname;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    static UserEntity from(User user) {
        return new UserEntity(
            user.id(),
            user.email(),
            user.nickname(),
            user.createdAt(),
            user.updatedAt()
        );
    }

    User toUser() {
        return User.builder()
            .id(id)
            .email(email)
            .nickname(nickname)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
    }

}
