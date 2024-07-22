package roomescape.domain.time.service.dto;

public class TimeResponse {

    private final Long id;
    private final String startAt;
    private final boolean reserved;

    public TimeResponse(Long id, String startAt, boolean reserved) {
        this.id = id;
        this.startAt = startAt;
        this.reserved = reserved;
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
