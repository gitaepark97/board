package board.backend.common.support.fake;

import board.backend.common.support.IdProvider;

public class FakeIdProvider implements IdProvider {

    private Long fixedId;

    public FakeIdProvider(Long fixedId) {
        this.fixedId = fixedId;
    }

    @Override
    public Long nextId() {
        return fixedId;
    }

    public void setId(Long newId) {
        this.fixedId = newId;
    }

}
