package board.backend.common.web.security;

import board.backend.auth.application.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TokenFilterTest {

    private AuthService authService;
    private TokenFilter tokenFilter;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        tokenFilter = new TokenFilter(authService);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Authorization 헤더가 없으면 인증하지 않고 다음 필터로 넘어간다")
    void noAuthorizationHeader() throws ServletException, IOException {
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        // when
        tokenFilter.doFilterInternal(request, response, chain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("유효하지 않은 토큰이면 인증하지 않고 다음 필터로 넘어간다")
    void invalidToken() throws ServletException, IOException {
        // given
        String token = "invalid.token";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(authService.getUserId(token)).thenReturn(Optional.empty());

        // when
        tokenFilter.doFilterInternal(request, response, chain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("유효한 토큰이면 인증 객체를 등록한다")
    void validToken() throws ServletException, IOException {
        // given
        String token = "valid.token";
        Long userId = 100L;
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        when(authService.getUserId(token)).thenReturn(Optional.of(userId));

        // when
        tokenFilter.doFilterInternal(request, response, chain);

        // then
        assertThat(SecurityContextHolder.getContext().getAuthentication())
            .isNotNull()
            .extracting(Authentication::getPrincipal)
            .isEqualTo(userId);
    }

}