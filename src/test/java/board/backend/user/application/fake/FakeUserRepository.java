package board.backend.user.application.fake;

import board.backend.user.application.port.UserRepository;
import board.backend.user.domain.User;

import java.util.*;

public class FakeUserRepository implements UserRepository {

    private final Map<Long, User> idStore = new HashMap<>();
    private final Map<String, User> emailStore = new HashMap<>();

    @Override
    public boolean existsById(Long id) {
        return idStore.containsKey(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return emailStore.containsKey(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(idStore.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(emailStore.get(email));
    }

    @Override
    public List<User> findAllById(List<Long> ids) {
        List<User> result = new ArrayList<>();
        for (Long id : ids) {
            User user = idStore.get(id);
            if (user != null) result.add(user);
        }
        return result;
    }

    @Override
    public void save(User user) {
        idStore.put(user.id(), user);
        emailStore.put(user.email(), user);
    }

}
