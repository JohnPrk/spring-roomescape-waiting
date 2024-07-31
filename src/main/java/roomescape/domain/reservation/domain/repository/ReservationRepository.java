package roomescape.domain.reservation.domain.repository;

import org.springframework.data.repository.query.Param;
import roomescape.domain.reservation.domain.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Optional<Reservation> findById(long reservationId);

    Long save(Reservation reservation);

    List<Reservation> findAll();

    void delete(Reservation reservation);

    List<Reservation> findAllByMemberId(@Param("memberId") Long memberId);
}
