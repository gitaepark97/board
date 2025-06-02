package board.backend.infra;

interface CustomUserRepository {

    boolean customExistsById(Long id);

    boolean existsByEmail(String email);

}
