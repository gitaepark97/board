package board.backend.application;

import board.backend.domain.LoginInfo;
import board.backend.domain.LoginInfoNotFound;
import board.backend.domain.LoginMethod;
import board.backend.domain.WrongPassword;
import board.backend.infra.LoginInfoRepository;
import board.backend.infra.PasswordEncoderProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class LoginInfoReader {

    private final PasswordEncoderProvider passwordEncoderProvider;
    private final LoginInfoRepository loginInfoRepository;

    LoginInfo read(String email, String password) {
        // 로그인 정보 조회
        LoginInfo loginInfo = loginInfoRepository.findByLoginMethodAndLoginKey(LoginMethod.EMAIL, email)
            .orElseThrow(LoginInfoNotFound::new);

        // 비밀번호 확인
        if (!passwordEncoderProvider.matches(password, loginInfo.getPassword())) {
            throw new WrongPassword();
        }

        return loginInfo;
    }

}
