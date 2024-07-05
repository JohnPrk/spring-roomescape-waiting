package roomescape.domain.reservation.service.dto;

import roomescape.domain.member.domain.Member;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.time.domain.Time;

public record ReservationResponse(Long id, String name, String date, String status, Time time, Theme theme,
                                  Member member) {
}
