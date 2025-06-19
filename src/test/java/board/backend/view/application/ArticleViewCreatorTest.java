package board.backend.view.application;

import board.backend.view.application.fake.FakeArticleViewCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleViewCreatorTest {

    private FakeArticleViewCountRepository articleViewCountRepository;
    private ArticleViewCreator articleViewCreator;

    @BeforeEach
    void setUp() {
        articleViewCountRepository = new FakeArticleViewCountRepository();
        articleViewCreator = new ArticleViewCreator(articleViewCountRepository);
    }

    @Test
    @DisplayName("게시글 ID로 조회 수를 초기화할 수 있다")
    void createCount_success_whenArticleIdGiven_savesInitialViewCount() {
        // given
        Long articleId = 1L;

        // when
        articleViewCreator.createCount(articleId);

        // then
        assertThat(articleViewCountRepository.findById(articleId)).isEqualTo(0L);
    }

}