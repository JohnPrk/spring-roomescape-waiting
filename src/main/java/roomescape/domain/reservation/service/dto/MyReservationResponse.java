package roomescape.domain.reservation.service.dto;

public record MyReservationResponse(Long reservationId, String theme, String date, String time, String status) {
}
