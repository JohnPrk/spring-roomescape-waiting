package roomescape.domain.reservation.domain;

import jakarta.persistence.*;
import roomescape.domain.member.domain.Member;
import roomescape.domain.time.domain.Time;

@Entity
public class ReservationTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "time_id")
    private Time time;
}
