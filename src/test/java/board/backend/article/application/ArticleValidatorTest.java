package board.backend.article.application;

import board.backend.article.application.fake.FakeArticleRepository;
import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.common.cache.fake.FakeCachedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleValidatorTest {

    private FakeCachedRepository<Article, Long> cachedRepository;
    private FakeArticleRepository articleRepository;
    private ArticleValidator articleValidator;

    @BeforeEach
    void setUp() {
        cachedRepository = new FakeCachedRepository<>();
        articleRepository = new FakeArticleRepository();
        articleValidator = new ArticleValidator(cachedRepository, articleRepository);
    }

    @Test
    @DisplayName("캐시에 존재하면 예외가 발생하지 않는다")
    void checkArticleExistsOrThrow_success_whenExistsInCache_doesNotThrow() {
        // given
        Article article = Article.create(1L, 100L, 10L, "제목", "내용", LocalDateTime.now());
        cachedRepository.save(article.id(), article, Duration.ofMinutes(10));

        // when & then
        assertThatCode(() -> articleValidator.checkArticleExistsOrThrow(article.id()))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("DB에 존재하면 예외가 발생하지 않는다")
    void checkArticleExistsOrThrow_success_whenExistsInRepository_doesNotThrow() {
        // given
        Article article = Article.create(1L, 100L, 10L, "제목", "내용", LocalDateTime.now());
        articleRepository.save(article);

        // when & then
        assertThatCode(() -> articleValidator.checkArticleExistsOrThrow(article.id()))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("캐시와 DB에 모두 없으면 예외가 발생한다")
    void checkArticleExistsOrThrow_fail_whenNotFound_throwsArticleNotFound() {
        // given
        Long articleId = 1L;

        // when & then
        assertThatThrownBy(() -> articleValidator.checkArticleExistsOrThrow(articleId))
            .isInstanceOf(ArticleNotFound.class);
    }

}