package roomescape.time;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TimeRepository {

    @PersistenceContext
    private EntityManager em;

    public TimeRepository(EntityManager em) {
        this.em = em;
    }

    public List<Time> findAll() {
        return em.createQuery("SELECT t FROM Time t WHERE t.deleted = false", Time.class)
                .getResultList();
    }

    public Time save(Time time) {
        em.persist(time);
        return time;
    }

    public Time deleteById(Long id) {
        Time time =  em.find(Time.class, id);
        if(time!=null)
            time.setDeleted(true);
        return time;
    }

}
