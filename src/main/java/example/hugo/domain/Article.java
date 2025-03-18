package example.hugo.domain;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
public record Article(
    Long articleId,
    String title,
    String content,
    Long boardId,
    Long writerId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public static Article create(
        Long articleId,
        String title,
        String content,
        Long boardId,
        Long writerId,
        LocalDateTime now
    ) {
        return Article.builder()
            .articleId(articleId)
            .title(title)
            .content(content)
            .boardId(boardId)
            .writerId(writerId)
            .createdAt(now)
            .updatedAt(now)
            .build();
    }

    public Article update(String title, String content, LocalDateTime now) {
        return toBuilder()
            .title(title)
            .content(content)
            .updatedAt(now)
            .build();
    }

}
