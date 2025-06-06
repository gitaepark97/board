package board.backend.application;

import board.backend.infra.ArticleViewCountRepository;
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
    @DisplayName("조회수 증가가 실패하면 새로 저장한다")
    void increase_whenFails_thenSave() {
        // given
        Long articleId = 1L;
        when(articleViewCountRepository.increase(articleId)).thenReturn(0L);

        // when
        articleViewWriter.increase(articleId);
    }

    @Test
    @DisplayName("조회수 증가에 성공하면 저장하지 않는다")
    void increase_whenSucceeds_thenNoSave() {
        // given
        Long articleId = 1L;
        when(articleViewCountRepository.increase(articleId)).thenReturn(1L);

        // when
        articleViewWriter.increase(articleId);
    }

    @Test
    @DisplayName("게시글 ID로 조회수 데이터를 삭제한다")
    void deleteArticle_success() {
        // given
        Long articleId = 1L;

        // when
        articleViewWriter.deleteArticle(articleId);
    }

}