package board.backend.common.event;

public record ArticleReaderEvent(
    Long articleId,
    String ip
) {

}
