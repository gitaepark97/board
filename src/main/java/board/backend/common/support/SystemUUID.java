package board.backend.common.support;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
class SystemUUID implements UUIDProvider {

    @Override
    public String random() {
        return UUID.randomUUID().toString();
    }

}
