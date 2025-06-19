package board.backend.article.application;

import board.backend.article.application.fake.FakeArticleRepository;
import board.backend.article.domain.Article;
import board.backend.common.event.EventType;
import board.backend.common.event.fake.FakeEventPublisher;
import board.backend.common.event.payload.ArticleDeletedEventPayload;
import board.backend.common.infra.fake.FakeCachedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleDeleterTest {

    private FakeArticleRepository articleRepository;
    private FakeCachedRepository<Article, Long> cachedRepository;
    private FakeEventPublisher eventPublisher;
    private ArticleDeleter articleDeleter;

    @BeforeEach
    void setUp() {
        articleRepository = new FakeArticleRepository();
        cachedRepository = new FakeCachedRepository<>();
        eventPublisher = new FakeEventPublisher();
        articleDeleter = new ArticleDeleter(cachedRepository, articleRepository, eventPublisher);
    }

    @Test
    @DisplayName("작성자가 게시글을 삭제할 수 있다")
    void delete_success_whenWriterMatches_deletesArticleAndPublishesEvent() {
        // given
        Article article = Article.create(1L, 100L, 10L, "제목", "내용", LocalDateTime.now());
        articleRepository.save(article);
        cachedRepository.save(article.id(), article, Duration.ofMinutes(10));

        // when
        Optional<Long> result = articleDeleter.delete(article.id(), article.writerId());

        // then
        assertThat(result).contains(article.boardId());
        assertThat(articleRepository.findById(article.id())).isEmpty();
        assertThat(cachedRepository.findByKey(article.id())).isEmpty();
        assertThat(eventPublisher.getPublishedEvents())
            .containsExactly(new FakeEventPublisher.PublishedEvent(EventType.ARTICLE_DELETED, new ArticleDeletedEventPayload(article.id())));
    }

    @Test
    @DisplayName("게시글이 존재하지 않으면 Optional.empty를 반환한다")
    void delete_success_whenArticleNotExists_returnsEmpty() {
        // given
        Long articleId = 1L;
        Long userId = 10L;

        // when
        Optional<Long> result = articleDeleter.delete(articleId, userId);

        // then
        assertThat(result).isEmpty();
    }

}