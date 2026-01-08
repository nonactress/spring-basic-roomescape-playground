package roomescape.theme;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@SQLDelete(sql = "UPDATE theme SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    private boolean deleted = false;

    protected Theme() {
    }

    public Theme(String name, String description) {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
