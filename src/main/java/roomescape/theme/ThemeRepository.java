package roomescape.theme;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ThemeRepository {
    @PersistenceContext
    private EntityManager em;

    public ThemeRepository(EntityManager em) {
        this.em = em;
    }

    public List<Theme> findAll() {
        return em.createNamedQuery("SELECT t FROM Theme t WHERE t.deleted = false", Theme.class).getResultList();
    }

    public Theme save (Theme theme) {
        em.persist(theme);
        return theme;
    }

    public void deleteById(Long id) {
       em.remove(em.find(Theme.class, id));
    }
}

