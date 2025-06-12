package board.backend.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.modulith.NamedInterface;

import java.time.LocalDateTime;


@NamedInterface
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
public class User {

    @Id
    private Long id;

    private String email;

    private String nickname;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static User create(Long id, String email, String nickname, LocalDateTime now) {
        return User.builder()
            .id(id)
            .email(email)
            .nickname(nickname)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    public User update(String nickname, LocalDateTime now) {
        this.nickname = nickname;
        this.updatedAt = now;

        return this;
    }

}
