package board.backend.support;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UUIDProvider {

    public String random() {
        return UUID.randomUUID().toString();
    }

}
