package board.backend.view.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class DailyViewCountScheduler {

    private final ViewCountSnapshotCreator viewCountSnapshotCreator;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    void createSnapshots() {
        viewCountSnapshotCreator.createCountSnapshot();
    }

}
