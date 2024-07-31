package roomescape.domain.waiting.domain;

import jakarta.persistence.*;
import roomescape.domain.member.domain.Member;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.time.domain.Time;

@Entity
public class Waiting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "themeId", referencedColumnName = "id")
    private Theme theme;

    @ManyToOne
    @JoinColumn(name = "memberId", referencedColumnName = "id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "timeId", referencedColumnName = "id")
    private Time time;

    private String date;

    public Waiting() {
    }

    public Waiting(Long id, Theme theme, Member member, Time time, String date) {
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
