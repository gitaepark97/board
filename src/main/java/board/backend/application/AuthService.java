package board.backend.application;

import board.backend.application.dto.Token;
import board.backend.domain.LoginInfo;
import board.backend.domain.Session;
import board.backend.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserReader userReader;
    private final LoginInfoReader loginInfoReader;
    private final SessionReader sessionReader;
    private final UserWriter userWriter;
    private final LoginInfoWriter loginInfoWriter;
    private final SessionWriter sessionWriter;
    private final TokenProcessor tokenProcessor;

    public void register(String email, String password, String nickname) {
        // 회원 조회
        User user = userReader.read(email).orElseGet(() -> userWriter.create(email, nickname));

        // 로그인 정보 생성
        loginInfoWriter.create(email, password, user.getId());
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
        return tokenProcessor.getUserId(accessToken).filter(userReader::isUserExists);
    }

}
