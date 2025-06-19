package board.backend.auth.infra;

import board.backend.auth.application.port.PasswordEncoderProvider;
import org.springframework.stereotype.Component;

@Component
class BCryptPasswordEncoder implements PasswordEncoderProvider {

    private final org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();

    @Override
    public String encode(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean matches(String password, String encodedPassword) {
        return encoder.matches(password, encodedPassword);
    }

}
