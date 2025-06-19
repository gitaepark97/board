package board.backend.common.support.fake;

import board.backend.common.support.UUIDProvider;

public class FakeUUIDProvider implements UUIDProvider {

    private String fixedUUID;

    public FakeUUIDProvider(String fixedUUID) {
        this.fixedUUID = fixedUUID;
    }

    @Override
    public String random() {
        return fixedUUID;
    }

    public void setUUID(String newUUID) {
        this.fixedUUID = newUUID;
    }

}
