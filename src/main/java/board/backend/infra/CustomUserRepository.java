package board.backend.infra;

interface CustomUserRepository {

    boolean existsByEmail(String email);

}
