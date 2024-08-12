package roomescape.domain.reservation.service.dto;

import roomescape.domain.member.domain.Member;
import roomescape.domain.reservation.domain.Reservation;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.time.domain.Time;
import roomescape.domain.waiting.domain.WaitingRank;

public class ReservationResponse {

    private final Long id;
    private final String name;
    private final String date;
    private final String status;
    private final Time time;
    private final Theme theme;
    private final Member member;

    public ReservationResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.name = reservation.getName();
        this.date = reservation.getDate();
        this.status = reservation.getStatus();
        this.time = reservation.getTime();
        this.theme = reservation.getTheme();
        this.member = reservation.getMember();
    }

    public ReservationResponse(WaitingRank waitingRank) {
        this.id = waitingRank.getWaiting().getId();
        this.name = waitingRank.getWaiting().getMember().getName();
        this.date = waitingRank.getWaiting().getDate();
        this.status = String.format(waitingRank.getRank() + "번째 예약대기");
        this.time = waitingRank.getWaiting().getTime();
        this.theme = waitingRank.getWaiting().getTheme();
        this.member = waitingRank.getWaiting().getMember();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public Member getMember() {
        return member;
    }
}
