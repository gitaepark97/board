package board.backend.article.infra.jpa;

import board.backend.article.application.port.ArticleRepository;
import board.backend.article.domain.Article;
import board.backend.common.config.TestJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(ArticleRepositoryImpl.class)
class ArticleRepositoryTest extends TestJpaRepository {

    private final Long boardId = 1L;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    @DisplayName("게시글이 존재하면 true를 반환한다")
    void existsById_success_whenExists_returnsTrue() {
        // given
        Article article = Article.create(1L, boardId, 100L, "제목 ", "내용 ", LocalDateTime.now());
        articleRepository.save(article);

        // when
        boolean result = articleRepository.existsById(1L);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("게시글이 존재하지 않으면 false를 반환한다")
    void existsById_success_whenNotExists_returnsFalse() {
        // when
        boolean result = articleRepository.existsById(999L);

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("lastArticleId가 null이면 최신순으로 게시글을 pageSize만큼 조회한다")
    void findAllByBoardId_success_whenNoLastId_returnsFirstPage() {
        // given
        for (int i = 1; i <= 5; i++) {
            Article article = Article.create(
                (long) i,
                boardId,
                100L + i,
                "제목 " + i,
                "내용 " + i,
                LocalDateTime.now()
            );
            articleRepository.save(article);
        }

        // when
        List<Article> result = articleRepository.findAllByBoardId(1L, 3L);

        // then
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().id()).isEqualTo(5L);
    }

    @Test
    @DisplayName("lastArticleId가 존재하면 해당 ID 이전의 게시글을 pageSize만큼 조회한다")
    void findAllByBoardId_success_whenLastIdGiven_returnsNextPage() {
        // given
        for (int i = 1; i <= 5; i++) {
            Article article = Article.create(
                (long) i,
                boardId,
                100L + i,
                "제목 " + i,
                "내용 " + i,
                LocalDateTime.now()
            );
            articleRepository.save(article);
        }

        // when
        List<Article> result = articleRepository.findAllByBoardId(1L, 3L, 4L);

        // then
        assertThat(result).hasSize(3);
        assertThat(result.getFirst().id()).isLessThan(4L);
    }

}