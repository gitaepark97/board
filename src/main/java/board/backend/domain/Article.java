package board.backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "article")
public class Article {

    @Id
    private Long id;

    private Long boardId;

    private Long writerId;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static Article create(Long id, Long boardId, Long writerId, String title, String content) {
        LocalDateTime now = LocalDateTime.now();

        return Article.builder()
            .id(id)
            .boardId(boardId)
            .writerId(writerId)
            .title(title)
            .content(content)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    public Article update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();

        return this;
    }

}
