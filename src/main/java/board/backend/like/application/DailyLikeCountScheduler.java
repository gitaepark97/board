package board.backend.like.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class DailyLikeCountScheduler {

    private final LikeCountSnapshotCreator likeCountSnapshotCreator;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    void createSnapshots() {
        likeCountSnapshotCreator.createCountSnapshot();
    }

}
