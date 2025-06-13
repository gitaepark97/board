package board.backend.article.infra.jpa;

import board.backend.article.domain.Article;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "article")
class ArticleEntity {

    @Id
    private Long id;

    private Long boardId;

    private Long writerId;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    static ArticleEntity from(Article article) {
        return new ArticleEntity(
            article.id(),
            article.boardId(),
            article.writerId(),
            article.title(),
            article.content(),
            article.createdAt(),
            article.updatedAt()
        );
    }

    Article toArticle() {
        return Article.builder()
            .id(id)
            .boardId(boardId)
            .writerId(writerId)
            .title(title)
            .content(content)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
    }

}
