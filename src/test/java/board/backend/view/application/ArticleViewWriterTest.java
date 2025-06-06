package board.backend.view.application;

import board.backend.view.infra.ArticleViewCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ArticleViewWriterTest {

    private ArticleViewCountRepository articleViewCountRepository;
    private ArticleViewWriter articleViewWriter;

    @BeforeEach
    void setUp() {
        articleViewCountRepository = mock(ArticleViewCountRepository.class);
        articleViewWriter = new ArticleViewWriter(articleViewCountRepository);
    }

    @Test
    @DisplayName("조회수가 없으면 1로 초기화한다")
    void increaseCount_initializeWhenZero() {
        // given
        Long articleId = 1L;
        when(articleViewCountRepository.increase(articleId)).thenReturn(0L);

        // when
        articleViewWriter.increaseCount(articleId);
    }

    @Test
    @DisplayName("조회수가 존재하면 증가만 한다")
    void increaseCount_incrementWhenExists() {
        // given
        Long articleId = 1L;
        when(articleViewCountRepository.increase(articleId)).thenReturn(1L);

        // when
        articleViewWriter.increaseCount(articleId);
    }

    @Test
    @DisplayName("게시글 조회수 데이터를 삭제한다")
    void deleteArticle_deletesViewCount() {
        // given
        Long articleId = 1L;

        // when
        articleViewWriter.deleteArticle(articleId);
    }

}