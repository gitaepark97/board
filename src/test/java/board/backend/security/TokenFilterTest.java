package board.backend.security;

import board.backend.auth.application.TokenManager;
import board.backend.auth.application.fake.FakeTokenProvider;
import board.backend.common.cache.fake.FakeCachedRepository;
import board.backend.user.application.UserValidator;
import board.backend.user.application.fake.FakeUserRepository;
import board.backend.user.domain.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TokenFilterTest {

    private TokenFilter tokenFilter;
    private FakeTokenProvider tokenProvider;
    private TokenManager tokenManager;

    @BeforeEach
    void setUp() {
        tokenProvider = new FakeTokenProvider();
        tokenManager = new TokenManager(
            tokenProvider,
            new UserValidator(new FakeCachedRepository<>(), new FakeUserRepository())
        );
        tokenFilter = new TokenFilter(tokenManager);
    }

    @Test
    @DisplayName("Authorization 헤더가 없으면 인증하지 않는다")
    void doFilter_whenNoAuthHeader_shouldNotAuthenticate() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        // when
        tokenFilter.doFilterInternal(request, response, chain);

        // then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }

    @Test
    @DisplayName("유효하지 않은 토큰이면 인증하지 않는다")
    void doFilter_whenInvalidToken_shouldNotAuthenticate() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        // when
        tokenFilter.doFilterInternal(request, response, chain);

        // then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNull();
    }

    @Test
    @DisplayName("유효한 토큰이면 인증 정보를 설정한다")
    void doFilter_whenValidToken_shouldAuthenticate() throws ServletException, IOException {
        // given
        Long userId = 10L;
        String token = tokenProvider.issueToken(Map.of("userId", userId.toString()), null);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = new MockFilterChain();

        // userValidator가 통과할 수 있도록 user 등록
        FakeUserRepository userRepository = new FakeUserRepository();
        userRepository.save(User.create(userId, "email@test.com", "nick", LocalDateTime.now()));
        tokenManager = new TokenManager(tokenProvider, new UserValidator(
            new FakeCachedRepository<>(), userRepository
        ));
        tokenFilter = new TokenFilter(tokenManager);

        // when
        tokenFilter.doFilterInternal(request, response, chain);

        // then
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertThat(authentication).isNotNull();
        assertThat(authentication.getPrincipal()).isEqualTo(userId);
    }

}