package board.backend.hotArticle.application;

import board.backend.hotArticle.application.port.HotArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.NamedInterface;
import org.springframework.stereotype.Component;

import java.util.List;

@NamedInterface
@RequiredArgsConstructor
@Component
public class HotArticleReader {

    private final HotArticleRepository hotArticleRepository;

    public List<Long> readAll(String dateStr) {
        return hotArticleRepository.readAll(dateStr);
    }

}
