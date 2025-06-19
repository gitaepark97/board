package board.backend.security;

import board.backend.auth.application.TokenManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
class TokenFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";

    private final TokenManager tokenManager;

    @Override
    protected void doFilterInternal(
        @NotNull HttpServletRequest request,
        @NotNull HttpServletResponse response,
        @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        Optional<String> token = getTokenManager(request);
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        Optional<Long> userId = this.tokenManager.getUserId(token.get());
        if (userId.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(userId.get(), null, null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    private Optional<String> getTokenManager(HttpServletRequest request) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith(BEARER)) {
            return Optional.empty();
        }
        return Optional.of(authHeader.substring(BEARER.length()));
    }

}
