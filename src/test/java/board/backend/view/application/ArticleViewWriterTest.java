package board.backend.view.application;

import board.backend.view.infra.ArticleViewCountRepository;
import board.backend.view.infra.ArticleViewDistributedLockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleViewWriterTest {

    private ArticleViewDistributedLockRepository articleViewDistributedLockRepository;
    private ArticleViewWriter articleViewWriter;

    @BeforeEach
    void setUp() {
        ArticleViewCountRepository articleViewCountRepository = mock(ArticleViewCountRepository.class);
        articleViewDistributedLockRepository = mock(ArticleViewDistributedLockRepository.class);
        articleViewWriter = new ArticleViewWriter(articleViewCountRepository, articleViewDistributedLockRepository);
    }

    @Test
    @DisplayName("락 획득 실패 시 조회수를 증가시키지 않는다")
    void increaseCount_doNothingWhenLockFails() {
        // given
        Long articleId = 1L;
        String ip = "127.0.0.1";
        when(articleViewDistributedLockRepository.lock(articleId, ip, Duration.ofMinutes(10))).thenReturn(false);

        // when
        articleViewWriter.increaseCount(articleId, ip);
    }

    @Test
    @DisplayName("조회 수 증가 결과가 0이면 초기값을 저장한다")
    void increaseCount_saveWhenResultIsZero() {
        // given
        Long articleId = 1L;
        String ip = "127.0.0.1";
        when(articleViewDistributedLockRepository.lock(articleId, ip, Duration.ofMinutes(10))).thenReturn(true);

        // when
        articleViewWriter.increaseCount(articleId, ip);
    }

    @Test
    @DisplayName("조회 수 증가 결과가 0이 아니면 저장하지 않는다")
    void increaseCount_noSaveWhenResultIsNotZero() {
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