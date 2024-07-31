package roomescape.domain.time.service.dto;

public class TimeWithStatus {

    private final Long id;
    private final String startAt;

    private final boolean reserved;

    public TimeWithStatus(Long id, String startAt, String reserved) {
        this.id = id;
        this.startAt = startAt;
        this.reserved = Boolean.parseBoolean(reserved);
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    public boolean isReserved() {
        return reserved;
    }
}
