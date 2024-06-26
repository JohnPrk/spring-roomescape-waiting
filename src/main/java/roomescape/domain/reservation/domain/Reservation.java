package roomescape.domain.reservation.domain;

import jakarta.persistence.*;
import roomescape.domain.time.domain.Time;

import java.util.List;

import static roomescape.utils.DateTimeCheckUtil.isBeforeCheck;
import static roomescape.utils.FormatCheckUtil.reservationDateFormatCheck;
import static roomescape.utils.FormatCheckUtil.reservationNameFormatCheck;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String date;

    @OneToMany(mappedBy = "reservation")
    private List<ReservationTheme> reservationThemes;

    @OneToMany(mappedBy = "reservation")
    private List<ReservationTime> reservationTimes;

    @OneToMany(mappedBy = "reservation")
    private List<ReservationMember> reservationMembers;

    public Reservation(Long id, String name, String date, Time time) {
        validationCheck(name, date, time);
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public Reservation() {
    }

    private static void validationCheck(String name, String date, Time time) {
        reservationNameFormatCheck(name);
        reservationDateFormatCheck(date);
        isBeforeCheck(date, time.getStartAt());
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
}
