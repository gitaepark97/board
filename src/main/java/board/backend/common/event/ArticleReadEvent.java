package board.backend.common.event;

public record ArticleReadEvent(
    Long articleId,
    String ip
) {

}
