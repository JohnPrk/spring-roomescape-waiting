package roomescape.domain.reservation.domain.repository;

import roomescape.domain.reservation.domain.Reservation;

import java.util.List;

public interface ReservationRepository {
    Reservation findById(long reservationId);

    Long save(Reservation reservation);

    List<Reservation> findAll();

    void delete(Long id);
}
