package example.hugo.domain;

public record BoardArticleCount(
    Long boardId,
    Long articleCount
) {

    public static BoardArticleCount init(Long boardId, Long articleCount) {
        return new BoardArticleCount(boardId, articleCount);
    }

}
