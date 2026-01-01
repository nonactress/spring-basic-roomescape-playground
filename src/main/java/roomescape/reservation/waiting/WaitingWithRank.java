package roomescape.reservation.waiting;

public class WaitingWithRank {
    private Waiting waiting;
    private Long rank;

    public WaitingWithRank(Waiting waiting, Long rank) {
        this.waiting = waiting;
        this.rank = rank + 1; // 0명 앞이면 "1번째"가 되어야 하므로 +1
    }

    public Waiting getWaiting() { return waiting; }
    public Long getRank() { return rank; }
}
