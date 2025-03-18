package example.hugo.infra.entity;

import example.hugo.domain.BoardArticleCount;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "board_article_count")
public class BoardArticleCountEntity {

    @Id
    private Long boardId;
    private Long articleCount;

    public static BoardArticleCountEntity from(BoardArticleCount boardArticleCount) {
        return BoardArticleCountEntity.builder()
            .boardId(boardArticleCount.boardId())
            .articleCount(boardArticleCount.articleCount())
            .build();
    }

    public BoardArticleCount toBoardArticleCount() {
        return new BoardArticleCount(boardId, articleCount);
    }

}
