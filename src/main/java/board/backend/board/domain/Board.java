package board.backend.board.domain;

import lombok.Builder;
import org.springframework.modulith.NamedInterface;

import java.time.LocalDateTime;

@NamedInterface
@Builder
public record Board(
    Long id,
    String title,
    LocalDateTime createdAt
) {

}
