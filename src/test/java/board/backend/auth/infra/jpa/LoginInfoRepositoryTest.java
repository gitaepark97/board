package board.backend.auth.infra.jpa;

import board.backend.auth.application.port.LoginInfoRepository;
import board.backend.auth.domain.LoginInfo;
import board.backend.auth.domain.LoginMethod;
import board.backend.common.infra.TestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Import(LoginInfoRepositoryImpl.class)
class LoginInfoRepositoryTest extends TestRepository {

    private final String email = "user1@example.com";

    @Autowired
    private LoginInfoRepository loginInfoRepository;

    @BeforeEach
    void setUp() {
        LoginInfo loginInfo = LoginInfo.create(1L, email, "password123!", 1L, LocalDateTime.now());
        loginInfoRepository.save(loginInfo);
    }

    @Test
    @DisplayName("로그인 정보가 존재하면 true를 반환한다")
    void existsBy_success_whenExists_returnsTrue() {
        // when
        boolean result = loginInfoRepository.existsBy(LoginMethod.EMAIL, email);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("로그인 정보가 존재하지 않으면 false를 반환한다")
    void existsBy_success_whenNotExists_returnsFalse() {
        // when
        boolean result = loginInfoRepository.existsBy(LoginMethod.EMAIL, "user999@example.com");

        // then
        assertThat(result).isFalse();
    }

}