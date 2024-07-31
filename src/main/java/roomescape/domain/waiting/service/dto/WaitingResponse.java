package roomescape.domain.waiting.service.dto;

import roomescape.domain.member.domain.Member;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.time.domain.Time;

public class WaitingResponse {

    private final Long id;

    private final Theme theme;

    private final Member member;

    private final Time time;

    private final String date;

    public WaitingResponse(Long id, Theme theme, Member member, Time time, String date) {
        this.id = id;
        this.theme = theme;
        this.member = member;
        this.time = time;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public Theme getTheme() {
        return theme;
    }

    public Member getMember() {
        return member;
    }

    public Time getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
