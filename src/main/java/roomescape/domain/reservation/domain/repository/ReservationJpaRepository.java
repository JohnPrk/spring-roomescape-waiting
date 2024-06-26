package roomescape.domain.reservation.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import roomescape.domain.reservation.domain.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {

    Optional<Reservation> findById(long reservationId);

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    void delete(Reservation reservation);
}
