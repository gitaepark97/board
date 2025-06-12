package board.backend.common.event;

public record RegisteredEvent(
    String email,
    String nickname
) {

}
