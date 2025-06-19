package board.backend.board.application.fake;

import board.backend.board.application.port.BoardRepository;
import board.backend.board.domain.Board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeBoardRepository implements BoardRepository {

    private final Map<Long, Board> store = new HashMap<>();

    @Override
    public boolean existsById(Long id) {
        return store.containsKey(id);
    }

    @Override
    public List<Board> findAll() {
        return new ArrayList<>(store.values());
    }

    public void save(Board board) {
        store.put(board.id(), board);
    }

}
