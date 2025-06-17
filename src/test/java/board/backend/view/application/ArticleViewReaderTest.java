package board.backend.view.application;

import board.backend.view.application.port.ArticleViewCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleViewReaderTest {

    private ArticleViewCountRepository articleViewCountRepository;
    private ArticleViewReader articleViewReader;

    @BeforeEach
    void setUp() {
        articleViewCountRepository = mock(ArticleViewCountRepository.class);
        articleViewReader = new ArticleViewReader(articleViewCountRepository);
    }

    @Test
    @DisplayName("게시글 ID로 조회 수 조회에 성공한다")
    void count_success() {
        // given
        Long articleId = 1L;
        Long viewCount = 1L;
        when(articleViewCountRepository.findById(articleId)).thenReturn(viewCount);

        // when
        Long result = articleViewReader.count(articleId);

        // then
        assertThat(result).isEqualTo(viewCount);
    }

    @Test
    @DisplayName("게시글 ID 목록으로 조회 수 목록 조회에 성공한다")
    void count_all_success() {
        // given
        List<Long> articleIds = List.of(1L, 2L);
        Map<Long, Long> cached = Map.of(
            1L, 1L, 2L, 2L
        );
        when(articleViewCountRepository.findAllById(articleIds)).thenReturn(cached);

        // when
        Map<Long, Long> result = articleViewReader.count(articleIds);

        // then
        assertThat(result).containsEntry(1L, 1L).containsEntry(2L, 2L);
    }

}