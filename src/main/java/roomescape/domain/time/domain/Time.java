package roomescape.domain.time.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Time {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String startAt;
    private boolean reserved;

    public Time(Long id, String startAt, boolean reserved) {
        this.id = id;
        this.startAt = startAt;
        this.reserved = reserved;
    }

    public Time() {
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void reserved() {
        this.reserved = true;
    }
}
