package board.backend.article.domain;

import board.backend.common.exception.Forbidden;
import lombok.Builder;
import org.springframework.modulith.NamedInterface;

import java.time.LocalDateTime;

@NamedInterface
@Builder(toBuilder = true)
public record Article(
    Long id,
    Long boardId,
    Long writerId,
    String title,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

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

        return toBuilder()
            .title(title)
            .content(content)
            .updatedAt(now)
            .build();
    }

    public void checkIsWriter(Long userId) {
        if (userId.longValue() != writerId.longValue()) {
            throw new Forbidden();
        }
    }

}
