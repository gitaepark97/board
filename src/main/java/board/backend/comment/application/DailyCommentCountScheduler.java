package board.backend.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class DailyCommentCountScheduler {

    private final CommentCountSnapshotCreator commentCountSnapshotCreator;

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    void createSnapshots() {
        commentCountSnapshotCreator.createCountSnapshot();
    }

}
