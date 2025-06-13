package board.backend.auth.application;

import board.backend.auth.application.port.LoginInfoRepository;
import board.backend.auth.domain.LoginInfo;
import board.backend.auth.domain.LoginInfoDuplicated;
import board.backend.auth.domain.LoginMethod;
import board.backend.auth.infra.PasswordEncoderProvider;
import board.backend.common.support.IdProvider;
import board.backend.common.support.TimeProvider;
import board.backend.user.application.UserReader;
import board.backend.user.application.UserWriter;
import board.backend.user.domain.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class LoginInfoWriter {

    private final IdProvider idProvider;
    private final TimeProvider timeProvider;
    private final LoginInfoRepository loginInfoRepository;
    private final PasswordEncoderProvider passwordEncoderProvider;
    private final UserReader userReader;
    private final UserWriter userWriter;

    @Transactional
    void create(String email, String password, String nickname) {
        // 회원 조회
        User user = userReader.read(email).orElseGet(() -> userWriter.create(email, nickname));

        // 로그인 정보 중복 확인
        checkLoginInfoUniqueOrThrows(email);

        // 로그인 정보 생성
        LoginInfo newLoginInfo = LoginInfo.create(idProvider.nextId(), email, passwordEncoderProvider.encode(password), user.id(), timeProvider.now());
        // 로그인 정보 저장
        loginInfoRepository.save(newLoginInfo);
    }

    private void checkLoginInfoUniqueOrThrows(String loginKey) {
        if (loginInfoRepository.existsBy(LoginMethod.EMAIL, loginKey)) {
            throw new LoginInfoDuplicated();
        }
    }

}
