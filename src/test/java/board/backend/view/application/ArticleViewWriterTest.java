package board.backend.view.application;

import board.backend.common.support.TimeProvider;
import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.application.port.ArticleViewDistributedLockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Duration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleViewWriterTest {

    private ArticleViewDistributedLockRepository articleViewDistributedLockRepository;
    private ArticleViewWriter articleViewWriter;

    @BeforeEach
    void setUp() {
        TimeProvider timeProvider = mock(TimeProvider.class);
        ArticleViewCountRepository articleViewCountRepository = mock(ArticleViewCountRepository.class);
        articleViewDistributedLockRepository = mock(ArticleViewDistributedLockRepository.class);
        ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
        articleViewWriter = new ArticleViewWriter(timeProvider, articleViewCountRepository, articleViewDistributedLockRepository, applicationEventPublisher);
    }

    @Test
    @DisplayName("조회 수 저장에 성공한다")
    void saveCount_success() {
        // given
        Long articleId = 1L;

        // when
        articleViewWriter.saveCount(articleId);
    }

    @Test
    @DisplayName("락 획득 실패 시 조회 수를 증가시키지 않는다")
    void increaseCount_doNothingWhenLockFails() {
        // given
        Long articleId = 1L;
        String ip = "127.0.0.1";
        when(articleViewDistributedLockRepository.lock(articleId, ip, Duration.ofMinutes(10))).thenReturn(false);

        // when
        articleViewWriter.increaseCount(articleId, ip);
    }

    @Test
    @DisplayName("조회 수를 증가시킨다")
    void increaseCount_success() {
        // given
        Long articleId = 1L;
        String ip = "127.0.0.1";
        when(articleViewDistributedLockRepository.lock(articleId, ip, Duration.ofMinutes(10))).thenReturn(true);

        // when
        articleViewWriter.increaseCount(articleId, ip);
    }

    @Test
    @DisplayName("게시글 조회 수 데이터를 삭제한다")
    void deleteArticle_success() {
        // given
        Long articleId = 1L;

        // when
        articleViewWriter.deleteArticle(articleId);
    }

}