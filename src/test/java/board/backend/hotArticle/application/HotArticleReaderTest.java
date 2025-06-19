package board.backend.hotArticle.application;

import board.backend.hotArticle.application.fake.FakeHotArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class HotArticleReaderTest {

    private FakeHotArticleRepository hotArticleRepository;
    private HotArticleReader hotArticleReader;

    @BeforeEach
    void setUp() {
        hotArticleRepository = new FakeHotArticleRepository();
        hotArticleReader = new HotArticleReader(hotArticleRepository);
    }

    @Test
    @DisplayName("특정 날짜의 핫 게시글 ID 목록을 조회할 수 있다")
    void readAll_success_returnsHotArticleIdsByDate() {
        // given
        String dateStr = "2024-01-01";
        hotArticleRepository.save(1L, LocalDateTime.parse("2024-01-01T10:00:00"), 100L, 10L, Duration.ofHours(1));
        hotArticleRepository.save(2L, LocalDateTime.parse("2024-01-01T11:00:00"), 120L, 10L, Duration.ofHours(1));
        hotArticleRepository.save(3L, LocalDateTime.parse("2024-01-01T11:00:00"), 200L, 10L, Duration.ofHours(1));

        // when
        List<Long> result = hotArticleReader.readAll(dateStr);

        // then
        assertThat(result).containsExactlyInAnyOrder(3L, 2L, 1L);
    }

}