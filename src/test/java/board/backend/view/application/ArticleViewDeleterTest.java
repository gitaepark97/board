package board.backend.view.application;

import board.backend.view.application.fake.FakeArticleViewCountBackupRepository;
import board.backend.view.application.fake.FakeArticleViewCountRepository;
import board.backend.view.domain.ArticleViewCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleViewDeleterTest {

    private FakeArticleViewCountRepository viewCountRepository;
    private FakeArticleViewCountBackupRepository backupRepository;
    private ArticleViewDeleter articleViewDeleter;

    @BeforeEach
    void setUp() {
        viewCountRepository = new FakeArticleViewCountRepository();
        backupRepository = new FakeArticleViewCountBackupRepository();
        articleViewDeleter = new ArticleViewDeleter(viewCountRepository, backupRepository);
    }

    @Test
    @DisplayName("게시글 ID로 조회 수 및 백업 데이터를 삭제할 수 있다")
    void deleteArticle_success_whenArticleExists_deletesBothRepositories() {
        // given
        ArticleViewCount count = ArticleViewCount.builder().articleId(1L).count(10L).build();
        viewCountRepository.save(count);
        backupRepository.save(count);

        // when
        articleViewDeleter.deleteArticle(count.getArticleId());

        // then
        assertThat(viewCountRepository.findById(count.getArticleId())).isEmpty();
        assertThat(backupRepository.findById(count.getArticleId())).isEmpty();
    }

}