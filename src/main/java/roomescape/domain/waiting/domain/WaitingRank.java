package roomescape.domain.waiting.domain;

public class WaitingRank {

    private final Waiting waiting;
    private final Long rank;

    public WaitingRank(Waiting waiting, Long rank) {
        this.waiting = waiting;
        this.rank = rank;
    }

    public Waiting getWaiting() {
        return waiting;
    }

    public Long getRank() {
        return rank;
    }
}
