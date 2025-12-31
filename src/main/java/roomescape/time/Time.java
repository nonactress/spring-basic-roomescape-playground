package roomescape.time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Time {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String value;

    private boolean deleted = false; // Soft Delete를 위한 필드

    public Time(Long id, String value) {
        this.id = id;
        this.value = value;
    }

    public Time(String value) {
        this.value = value;
    }

    public Time() {

    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
