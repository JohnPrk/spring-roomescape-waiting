package roomescape.domain.reservation.domain;

import roomescape.domain.reservation.service.dto.ReservationResponse;

import java.util.List;

public class Reservations {

    private final List<Reservation> reservations;

    public Reservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<ReservationResponse> mapToReservationResponseDto() {
        return reservations.stream().map(ReservationResponse::new).toList();
    }
}
