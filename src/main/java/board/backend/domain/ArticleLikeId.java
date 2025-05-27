package board.backend.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record ArticleLikeId(
    Long articleId,
    Long userId
) implements Serializable {

}
