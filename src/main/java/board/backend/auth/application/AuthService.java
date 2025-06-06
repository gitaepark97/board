package board.backend.auth.application;

import board.backend.auth.application.dto.Token;
import board.backend.auth.domain.LoginInfo;
import board.backend.auth.domain.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final LoginInfoReader loginInfoReader;
    private final SessionReader sessionReader;
    private final LoginInfoWriter loginInfoWriter;
    private final SessionWriter sessionWriter;
    private final TokenProcessor tokenProcessor;

    public void register(String email, String password, String nickname) {
        loginInfoWriter.create(email, password, nickname);
    }

    public Token login(String email, String password) {
        // 로그인 정보 조회
        LoginInfo loginInfo = loginInfoReader.read(email, password);

        // 세션 생성
        Session session = sessionWriter.create(loginInfo.getUserId());

        // 토큰 발급
        return tokenProcessor.issueToken(session);
    }

    public void logout(Long userId) {
        // 세션 삭제
        sessionWriter.delete(userId);
    }

    public Token reissueToken(String refreshToken) {
        // 세션 조회
        Session session = sessionReader.read(refreshToken);

        // 토큰 발급
        return tokenProcessor.issueToken(session);
    }

    public Optional<Long> getUserId(String accessToken) {
        // 회원 ID 추출
        return tokenProcessor.getUserId(accessToken);
    }

}
