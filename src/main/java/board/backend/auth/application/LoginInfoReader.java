package board.backend.auth.application;

import board.backend.auth.application.port.LoginInfoRepository;
import board.backend.auth.application.port.PasswordEncoderProvider;
import board.backend.auth.domain.LoginInfo;
import board.backend.auth.domain.LoginInfoNotFound;
import board.backend.auth.domain.LoginMethod;
import board.backend.auth.domain.WrongPassword;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class LoginInfoReader {

    private final LoginInfoRepository loginInfoRepository;
    private final PasswordEncoderProvider passwordEncoderProvider;

    LoginInfo read(String email, String password) {
        // 로그인 정보 조회
        LoginInfo loginInfo = loginInfoRepository.findByLoginMethodAndLoginKey(LoginMethod.EMAIL, email)
            .orElseThrow(LoginInfoNotFound::new);

        // 비밀번호 확인
        if (!passwordEncoderProvider.matches(password, loginInfo.password())) {
            throw new WrongPassword();
        }

        return loginInfo;
    }

}
