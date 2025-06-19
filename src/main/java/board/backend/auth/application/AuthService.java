package board.backend.auth.application;

import board.backend.auth.application.dto.Token;
import board.backend.auth.domain.LoginInfo;
import board.backend.auth.domain.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final LoginInfoReader loginInfoReader;
    private final SessionReader sessionReader;
    private final LoginInfoCreator loginInfoCreator;
    private final SessionCreator sessionCreator;
    private final SessionDeleter sessionDeleter;
    private final TokenManager tokenManager;

    public void register(String email, String password, String nickname) {
        loginInfoCreator.create(email, password, nickname);
    }

    public Token login(String email, String password) {
        // 로그인 정보 조회
        LoginInfo loginInfo = loginInfoReader.read(email, password);

        // 기존 세션 삭제
        sessionDeleter.delete(loginInfo.userId());

        // 세션 생성
        Session session = sessionCreator.create(loginInfo.userId());

        // 토큰 발급
        return tokenManager.issueToken(session);
    }

    public void logout(Long userId) {
        // 세션 삭제
        sessionDeleter.delete(userId);
    }

    public Token reissueToken(String refreshToken) {
        // 세션 조회
        Session session = sessionReader.read(refreshToken);

        // 토큰 발급
        return tokenManager.issueToken(session);
    }

}
