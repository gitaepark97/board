package board.backend.article.application;

import board.backend.article.application.fake.FakeArticleRepository;
import board.backend.article.domain.Article;
import board.backend.article.domain.ArticleNotFound;
import board.backend.common.event.EventType;
import board.backend.common.event.fake.FakeEventPublisher;
import board.backend.common.infra.fake.FakeCachedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ArticleReaderTest {

    private FakeCachedRepository<Article, Long> cachedRepository;
    private FakeArticleRepository articleRepository;
    private FakeEventPublisher eventPublisher;
    private ArticleReader articleReader;

    @BeforeEach
    void setUp() {
        cachedRepository = new FakeCachedRepository<>();
        articleRepository = new FakeArticleRepository();
        eventPublisher = new FakeEventPublisher();
        articleReader = new ArticleReader(cachedRepository, articleRepository, eventPublisher);
    }

    @Test
    @DisplayName("게시판 ID로 게시글을 조회할 수 있다 (lastId가 없는 경우)")
    void readAll_success_whenNoLastId_returnsFirstPage() {
        // given
        Long boardId = 1L;
        Article article1 = Article.create(1L, boardId, 10L, "t1", "c1", LocalDateTime.now());
        Article article2 = Article.create(2L, boardId, 10L, "t2", "c2", LocalDateTime.now());
        articleRepository.save(article1);
        articleRepository.save(article2);

        // when
        List<Article> result = articleReader.readAll(boardId, 10L, null);

        // then
        assertThat(result).containsExactly(article2, article1);
    }

    @Test
    @DisplayName("게시판 ID로 게시글을 조회할 수 있다 (lastId가 있는 경우)")
    void readAll_success_whenLastIdGiven_returnsNextPage() {
        // given
        Long boardId = 1L;
        Article article3 = Article.create(3L, boardId, 10L, "t1", "c1", LocalDateTime.now());
        Article article4 = Article.create(4L, boardId, 10L, "t2", "c2", LocalDateTime.now());
        articleRepository.save(article3);
        articleRepository.save(article4);

        // when
        List<Article> result = articleReader.readAll(boardId, 2L, 5L);

        // then
        assertThat(result).containsExactly(article4, article3);
    }

    @Test
    @DisplayName("ID 목록으로 게시글을 조회할 수 있다 (캐시 포함)")
    void readAll_success_whenSomeCached_returnsAllAndCachesMissed() {
        // given
        Article article1 = Article.create(1L, 1L, 10L, "t1", "c1", LocalDateTime.now());
        Article article2 = Article.create(2L, 1L, 10L, "t2", "c2", LocalDateTime.now());
        cachedRepository.save(1L, article1, Duration.ofMinutes(10));
        articleRepository.save(article2);

        // when
        List<Article> result = articleReader.readAll(List.of(1L, 2L));

        // then
        assertThat(result).containsExactly(article1, article2);
        assertThat(cachedRepository.findByKey(2L)).contains(article2);
    }

    @Test
    @DisplayName("게시글을 조회하고 조회 이벤트를 발행한다")
    void read_success_whenArticleExists_publishesReadEvent() {
        // given
        String ip = "127.0.0.1";
        Article article = Article.create(1L, 1L, 10L, "title", "content", LocalDateTime.now());
        articleRepository.save(article);

        // when
        Article result = articleReader.read(article.id(), ip);

        // then
        assertThat(result).isEqualTo(article);
        assertThat(eventPublisher.getPublishedEvents().getFirst().type()).isEqualTo(EventType.ARTICLE_READ);
        assertThat(cachedRepository.findByKey(article.id())).contains(article);
    }

    @Test
    @DisplayName("게시글이 존재하지 않으면 예외가 발생한다")
    void read_fail_whenArticleNotFound_throwsException() {
        // given
        Long id = 999L;

        // when & then
        assertThatThrownBy(() -> articleReader.read(id, "127.0.0.1"))
            .isInstanceOf(ArticleNotFound.class);
    }

}