package board.backend.article.application;

import board.backend.article.application.fake.FakeArticleRepository;
import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.common.infra.fake.FakeCachedRepository;
import board.backend.common.support.fake.FakeTimeProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleUpdaterTest {

    private final LocalDateTime now = LocalDateTime.of(2024, 1, 1, 12, 0);

    private FakeCachedRepository<Article, Long> cachedRepository;
    private FakeArticleRepository articleRepository;
    private ArticleUpdater articleUpdater;

    @BeforeEach
    void setUp() {
        cachedRepository = new FakeCachedRepository<>();
        articleRepository = new FakeArticleRepository();
        articleUpdater = new ArticleUpdater(new FakeTimeProvider(now), cachedRepository, articleRepository);
    }

    @Test
    @DisplayName("작성자가 게시글을 수정할 수 있다")
    void update_success_whenWriterUpdatesArticle_updatesAndDeletesCache() {
        // given
        String newTitle = "수정된 제목";
        String newContent = "수정된 내용";
        Article article = Article.create(1L, 100L, 10L, "제목", "내용", LocalDateTime.now());
        articleRepository.save(article);
        cachedRepository.save(article.id(), article, Duration.ofMinutes(10));

        // when
        Article updated = articleUpdater.update(article.id(), article.writerId(), newTitle, newContent);

        // then
        assertThat(updated.title()).isEqualTo(newTitle);
        assertThat(updated.content()).isEqualTo(newContent);
        assertThat(updated.updatedAt()).isEqualTo(now);

        assertThat(articleRepository.findById(article.id())).contains(updated);
        assertThat(cachedRepository.findByKey(article.id())).isEmpty();
    }

    @Test
    @DisplayName("게시글이 존재하지 않으면 예외가 발생한다")
    void update_fail_whenArticleNotFound_throwsException() {
        // given
        Long articleId = 1L;

        // when & then
        assertThatThrownBy(() -> articleUpdater.update(articleId, 10L, "t", "c"))
            .isInstanceOf(ArticleNotFound.class);
    }

}