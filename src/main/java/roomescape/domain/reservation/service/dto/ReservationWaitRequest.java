package roomescape.domain.reservation.service.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReservationWaitRequest {

    private final String date;
    private final Long timeId;
    private final Long themeId;

    @JsonCreator
    public ReservationWaitRequest(
            @JsonProperty("date") String date,
            @JsonProperty("time") Long timeId,
            @JsonProperty("theme") Long themeId) {
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public String getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }
}
