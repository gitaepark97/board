package board.backend.board.infra.jpa;

import board.backend.board.domain.Board;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "board")
class BoardEntity {

    @Id
    private Long id;

    private String title;

    private LocalDateTime createdAt;

    static BoardEntity from(Board board) {
        return new BoardEntity(
            board.id(),
            board.title(),
            board.createdAt()
        );
    }

    Board toBoard() {
        return Board.builder()
            .id(id)
            .title(title)
            .createdAt(createdAt)
            .build();
    }

}
