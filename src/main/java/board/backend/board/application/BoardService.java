package board.backend.board.application;

import board.backend.board.domain.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardReader boardReader;

    @Cacheable(value = "boardList")
    public List<Board> readAll() {
        return boardReader.readAll();
    }

}
