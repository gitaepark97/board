package board.backend.auth.application.port;

public interface PasswordEncoderProvider {

    String encode(String password);

    boolean matches(String password, String encodedPassword);

}
