package board.backend.domain;

import board.backend.support.Forbidden;
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

    public static Article create(
        Long id,
        Long boardId,
        Long writerId,
        String title,
        String content,
        LocalDateTime now
    ) {
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

    public Article update(Long userId, String title, String content, LocalDateTime now) {
        checkIsWriter(userId);

        this.title = title;
        this.content = content;
        this.updatedAt = now;

        return this;
    }

    public void checkIsWriter(Long userId) {
        if (userId.longValue() != writerId.longValue()) {
            throw new Forbidden();
        }
    }

}
