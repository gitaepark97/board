package board.backend.like.infra.jpa;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public record ArticleLikeId(
    Long articleId,
    Long userId
) implements Serializable {

}
