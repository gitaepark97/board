package example.hugo.domain;

public record BoardArticleCount(
    Long boardId,
    Long articleCount
) {

    public static BoardArticleCount init(Long boardId) {
        return new BoardArticleCount(boardId, 1L);
    }

}
