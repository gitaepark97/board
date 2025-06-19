package board.backend.view.application;

import board.backend.view.application.port.ArticleViewCountBackupRepository;
import board.backend.view.application.port.ArticleViewCountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ArticleViewDeleter {

    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleViewCountBackupRepository articleViewCountBackUpRepository;

    @Transactional
    void deleteArticle(Long articleId) {
        articleViewCountBackUpRepository.deleteById(articleId);
        articleViewCountRepository.deleteById(articleId);
    }

}
