package info.pionas.ideas.common.uuid;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidUtils {

    public UUID generate() {
        return UUID.randomUUID();
    }
}
