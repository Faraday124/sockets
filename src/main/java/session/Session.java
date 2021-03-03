package session;

import java.util.UUID;

public class Session {

    private final long startTime;
    private final UUID uuid;
    private String name;

    public Session() {
        this.startTime = System.currentTimeMillis();
        this.uuid = UUID.randomUUID();
    }

    public long getSessionTime() {
        return System.currentTimeMillis() - startTime;
    }

    public String getName() {
        return name == null ? "anonymous" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }
}
