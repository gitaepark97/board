package board.backend.like.application;

import board.backend.like.domain.ArticleLikeCount;
import board.backend.like.infra.ArticleLikeCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleLikeReaderTest {

    private ArticleLikeCountRepository articleLikeCountRepository;
    private ArticleLikeReader articleLikeReader;

    @BeforeEach
    void setUp() {
        articleLikeCountRepository = mock(ArticleLikeCountRepository.class);
        articleLikeReader = new ArticleLikeReader(articleLikeCountRepository);
    }

    @Test
    @DisplayName("게시글 ID 목록으로 좋아요 수를 조회한다")
    void count_success() {
        // given
        ArticleLikeCount like1 = ArticleLikeCount.init(1L);
        ArticleLikeCount like2 = ArticleLikeCount.init(2L);
        when(articleLikeCountRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(like1, like2));

        // when
        Map<Long, Long> result = articleLikeReader.count(List.of(1L, 2L));

        // then
        assertThat(result.get(1L)).isEqualTo(1L);
        assertThat(result.get(2L)).isEqualTo(1L);
    }

}