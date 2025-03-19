package example.hugo.infra.entity;

import example.hugo.domain.Article;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity(name = "article")
@DynamicUpdate
public class ArticleEntity {

    @Id
    private Long articleId;
    private String title;
    private String content;
    private Long boardId;
    private Long writerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ArticleEntity from(Article article) {
        return ArticleEntity.builder()
            .articleId(article.articleId())
            .title(article.title())
            .content(article.content())
            .boardId(article.boardId())
            .writerId(article.writerId())
            .createdAt(article.createdAt())
            .updatedAt(article.updatedAt())
            .build();
    }

    public Article toArticle() {
        return Article.builder()
            .articleId(articleId)
            .title(title)
            .content(content)
            .boardId(boardId)
            .writerId(writerId)
            .createdAt(createdAt)
            .updatedAt(updatedAt)
            .build();
    }

}
