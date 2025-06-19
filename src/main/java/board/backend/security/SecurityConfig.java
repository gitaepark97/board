package board.backend.security;

import board.backend.auth.application.TokenManager;
import board.backend.common.exception.ApplicationException;
import board.backend.common.web.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
class SecurityConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final TokenManager tokenManager;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests.requestMatchers(HttpMethod.GET, "/", "/docs/**", "/actuator/**")
                .permitAll()
                .requestMatchers(HttpMethod.GET, "/api/*/my")
                .authenticated()
                .requestMatchers(HttpMethod.GET)
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login", "/api/auth/reissue-token")
                .permitAll()
                .anyRequest()
                .authenticated())
            .addFilterBefore(new TokenFilter(tokenManager), UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(exception -> exception.authenticationEntryPoint(authenticationEntryPoint()))
            .build();
    }

    private AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            ApplicationException exception = new ApplicationException(401, "인증에 실패했습니다.");
            ApiResponse<?> apiResponse = ApiResponse.of(exception);
            response.setStatus(apiResponse.status().value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        };
    }

    private CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());

        return request -> {
            CorsConfiguration config = new CorsConfiguration();
            String origin = request.getHeader("Origin");

            if (origin != null) {
                config.setAllowedOrigins(List.of(origin));
            }

            config.setAllowedMethods(List.of("HEAD", "POST", "GET", "DELETE", "PUT"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);

            return config;
        };
    }

}
