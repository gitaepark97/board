package board.backend.common.support.fake;

import board.backend.common.support.TimeProvider;

import java.time.LocalDateTime;

public class FakeTimeProvider implements TimeProvider {

    private LocalDateTime fixedTime;

    public FakeTimeProvider(LocalDateTime fixedTime) {
        this.fixedTime = fixedTime;
    }

    @Override
    public LocalDateTime now() {
        return fixedTime;
    }

    public void setTime(LocalDateTime newTime) {
        this.fixedTime = newTime;
    }

}
