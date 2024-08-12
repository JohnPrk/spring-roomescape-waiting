package roomescape.domain.waiting.domain;

import roomescape.domain.reservation.service.dto.ReservationResponse;

import java.util.List;

public class WaitingRanks {

    private final List<WaitingRank> waitingRanks;

    public WaitingRanks(List<WaitingRank> waitingRanks) {
        this.waitingRanks = waitingRanks;
    }

    public List<ReservationResponse> mapToReservationResponseDto() {
        return waitingRanks.stream().map(ReservationResponse::new).toList();
    }
}
