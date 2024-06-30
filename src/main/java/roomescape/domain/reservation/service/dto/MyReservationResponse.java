package roomescape.domain.reservation.service.dto;

import roomescape.domain.theme.domain.Theme;
import roomescape.domain.time.domain.Time;

public record MyReservationResponse(Long reservationId, Theme theme, String date, Time time, String status) {
}
