package board.backend.auth.application.fake;

import board.backend.auth.application.port.PasswordEncoderProvider;

public class FakePasswordEncoderProvider implements PasswordEncoderProvider {

    private static final String PREFIX = "encoded:";

    @Override
    public String encode(String password) {
        return PREFIX + password;
    }

    @Override
    public boolean matches(String password, String encodedPassword) {
        return encodedPassword.equals(PREFIX + password);
    }

}
