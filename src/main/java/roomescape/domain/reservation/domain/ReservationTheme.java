package roomescape.domain.reservation.domain;

import jakarta.persistence.*;
import roomescape.domain.theme.domain.Theme;
import roomescape.domain.time.domain.Time;

@Entity
public class ReservationTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;
}
