package board.backend.like.application;

import board.backend.like.application.fake.FakeArticleLikeCountRepository;
import board.backend.like.application.fake.FakeArticleLikeRepository;
import board.backend.like.domain.ArticleLike;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


class ArticleLikeDeleterTest {

    private FakeArticleLikeRepository articleLikeRepository;
    private FakeArticleLikeCountRepository articleLikeCountRepository;
    private ArticleLikeDeleter articleLikeDeleter;

    @BeforeEach
    void setUp() {
        articleLikeRepository = new FakeArticleLikeRepository();
        articleLikeCountRepository = new FakeArticleLikeCountRepository();
        articleLikeDeleter = new ArticleLikeDeleter(articleLikeRepository, articleLikeCountRepository);
    }

    @Test
    @DisplayName("게시글 ID로 좋아요 및 좋아요 수를 삭제할 수 있다")
    void deleteArticle_success_whenValidArticleId_deletesLikesAndLikeCount() {
        // given
        Long articleId = 1L;
        ArticleLike like1 = new ArticleLike(1L, 10L, LocalDateTime.now());
        ArticleLike like2 = new ArticleLike(1L, 20L, LocalDateTime.now());
        articleLikeRepository.save(like1);
        articleLikeRepository.save(like2);
        articleLikeCountRepository.increase(articleId);

        // when
        articleLikeDeleter.deleteArticle(articleId);

        // then
        assertThat(articleLikeRepository.findByArticleIdAndUserId(articleId, 10L)).isEmpty();
        assertThat(articleLikeRepository.findByArticleIdAndUserId(articleId, 20L)).isEmpty();
        assertThat(articleLikeCountRepository.findById(articleId)).isEmpty();
    }

}