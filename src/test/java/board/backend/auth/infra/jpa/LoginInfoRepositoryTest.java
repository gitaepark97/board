package board.backend.auth.infra.jpa;

import board.backend.auth.application.port.LoginInfoRepository;
import board.backend.auth.domain.LoginInfo;
import board.backend.auth.domain.LoginMethod;
import board.backend.common.config.TestJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Import(LoginInfoRepositoryImpl.class)
class LoginInfoRepositoryTest extends TestJpaRepository {

    @Autowired
    private LoginInfoRepository loginInfoRepository;

    @Test
    @DisplayName("로그인 정보가 존재하면 true를 반환한다")
    void existsBy_success_whenExists_returnsTrue() {
        // given
        LoginInfo loginInfo = LoginInfo.create(1L, "user1@example.com", "password123!", 1L, LocalDateTime.now());
        loginInfoRepository.save(loginInfo);

        // when
        boolean result = loginInfoRepository.existsBy(LoginMethod.EMAIL, loginInfo.key());

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