package board.backend.infra;

import board.backend.domain.Article;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(CustomArticleRepositoryImpl.class)
class ArticleRepositoryTest extends TestRepository {

    @Autowired
    private ArticleRepository articleRepository;

    @BeforeEach
    void setUp() {
        // given: 게시글 5개 저장 (ID는 자동 증가 가정)
        for (int i = 1; i <= 5; i++) {
            Article article = Article.create(
                (long) i,
                1L,
                100L + i,
                "제목 " + i,
                "내용 " + i,
                LocalDateTime.now()
            );
            articleRepository.save(article);
        }
    }

    @Test
    @DisplayName("articleId로 게시글 존재 여부를 확인한다 - 존재함")
    void customExistsById_exists() {
        // when
        Long articleId = 1L;
        boolean result = articleRepository.customExistsById(articleId);

        // then
        AssertionsForClassTypes.assertThat(result).isTrue();
    }

    @Test
    @DisplayName("articleId로 게시글 존재 여부를 확인한다 - 존재하지 않음")
    void customExistsById_notExists() {
        // when
        boolean result = articleRepository.customExistsById(999L);

        // then
        AssertionsForClassTypes.assertThat(result).isFalse();
    }

    @Test
    @DisplayName("lastArticleId가 null인 경우 최신순으로 게시글 pageSize만큼 조회")
    void findAllByBoardId_without_last_article_id() {
        // when
        List<Article> result = articleRepository.findAllByBoardId(1L, 3L);

        // then
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().getId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("lastArticleId가 존재할 경우 해당 ID 이전의 게시글을 pageSize만큼 조회")
    void findAllByBoardId_with_last_article_id() {
        // when
        List<Article> result = articleRepository.findAllByBoardId(1L, 3L, 4L);

        // then
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().getId()).isLessThan(4L);
    }

}