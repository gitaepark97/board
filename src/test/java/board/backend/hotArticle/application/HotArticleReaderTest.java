package board.backend.hotArticle.application;

import board.backend.hotArticle.application.port.HotArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HotArticleReaderTest {

    private HotArticleRepository hotArticleRepository;
    private HotArticleReader hotArticleReader;

    @BeforeEach
    void setUp() {
        hotArticleRepository = mock(HotArticleRepository.class);
        hotArticleReader = new HotArticleReader(hotArticleRepository);
    }

    @Test
    @DisplayName("지정된 날짜에 해당하는 인기 게시글 ID 목록을 조회한다")
    void readAll_shouldReturnHotArticleIds() {
        // given
        String dateStr = "2024-06-06";
        List<Long> articleIds = List.of(1L, 2L, 3L);
        when(hotArticleRepository.readAll(dateStr)).thenReturn(articleIds);

        // when
        List<Long> result = hotArticleReader.readAll(dateStr);

        // then
        assertThat(result).isEqualTo(articleIds);
    }

}