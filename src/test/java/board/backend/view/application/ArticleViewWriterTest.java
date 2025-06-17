package board.backend.view.application;

import board.backend.common.support.TimeProvider;
import board.backend.view.application.port.ArticleViewBackupTimeRepository;
import board.backend.view.application.port.ArticleViewCountBackupRepository;
import board.backend.view.application.port.ArticleViewCountRepository;
import board.backend.view.application.port.ArticleViewDistributedLockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleViewWriterTest {

    private TimeProvider timeProvider;
    private ArticleViewDistributedLockRepository articleViewDistributedLockRepository;
    private ArticleViewBackupTimeRepository articleViewBackupTimeRepository;
    private ArticleViewWriter articleViewWriter;

    @BeforeEach
    void setUp() {
        timeProvider = mock(TimeProvider.class);
        ArticleViewCountRepository articleViewCountRepository = mock(ArticleViewCountRepository.class);
        ArticleViewCountBackupRepository articleViewCountBackUpRepository = mock(ArticleViewCountBackupRepository.class);
        articleViewDistributedLockRepository = mock(ArticleViewDistributedLockRepository.class);
        articleViewBackupTimeRepository = mock(ArticleViewBackupTimeRepository.class);
        ApplicationEventPublisher applicationEventPublisher = mock(ApplicationEventPublisher.class);
        articleViewWriter = new ArticleViewWriter(timeProvider, articleViewCountRepository, articleViewCountBackUpRepository, articleViewDistributedLockRepository, articleViewBackupTimeRepository, applicationEventPublisher);
    }

    @Test
    @DisplayName("게시글 조회 수 생성에 성공한다")
    void save_success() {
        // given
        Long articleId = 1L;

        // when
        articleViewWriter.createCount(articleId);
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
        when(articleViewBackupTimeRepository.findById(articleId)).thenReturn(LocalDateTime.now());
        when(timeProvider.now()).thenReturn(LocalDateTime.now());

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