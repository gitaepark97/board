package board.backend.application;

import board.backend.domain.LoginInfo;
import board.backend.domain.LoginInfoDuplicated;
import board.backend.domain.LoginMethod;
import board.backend.infra.LoginInfoRepository;
import board.backend.infra.PasswordEncoderProvider;
import board.backend.support.IdProvider;
import board.backend.support.TimeProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class LoginInfoWriter {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final PasswordEncoderProvider passwordEncoderProvider;
    private final LoginInfoRepository loginInfoRepository;

    @Transactional
    void create(String email, String password, Long userId) {
        checkLoginInfoUniqueOrThrows(email);

        // 로그인 정보 생성
        LoginInfo newLoginInfo = LoginInfo.create(idProvider.nextId(), email, passwordEncoderProvider.encode(password), userId, timeProvider.now());
        // 로그인 정보 저장
        loginInfoRepository.save(newLoginInfo);
    }

    private void checkLoginInfoUniqueOrThrows(String loginKey) {
        if (loginInfoRepository.existsByLoginMethodAndLoginKey(LoginMethod.EMAIL, loginKey)) {
            throw new LoginInfoDuplicated();
        }
    }

}
