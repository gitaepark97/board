package board.backend.infra;

import board.backend.domain.LoginInfo;
import board.backend.domain.LoginMethod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@Import(CustomLoginInfoRepositoryImpl.class)
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
    @DisplayName("loginMethod와 loginKey로 로그인 정보 존재 여부를 확인한다 - 존재함")
    void existsByLoginMethodAndLoginKey_exists() {
        // when
        boolean result = loginInfoRepository.existsByLoginMethodAndLoginKey(LoginMethod.EMAIL, email);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("loginMethod와 loginKey로 로그인 정보 존재 여부를 확인한다 - 존재하지 않음")
    void existsByLoginMethodAndLoginKey_notExists() {
        // when
        boolean result = loginInfoRepository.existsByLoginMethodAndLoginKey(LoginMethod.EMAIL, "user999@example.com");

        // then
        assertThat(result).isFalse();
    }

}