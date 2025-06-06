package board.backend.user.infra;

interface CustomUserRepository {

    boolean customExistsById(Long id);

    boolean existsByEmail(String email);

}
