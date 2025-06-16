package board.backend.hotArticle.application;

import board.backend.hotArticle.application.port.HotArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class HotArticleWriter {

    private final HotArticleRepository hotArticleRepository;

    void delete(Long articleId) {
        hotArticleRepository.delete(articleId);
    }

}
