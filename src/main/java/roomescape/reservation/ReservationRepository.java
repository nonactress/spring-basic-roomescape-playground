package roomescape.reservation;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.theme.Theme;
import roomescape.time.Time;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationRepository {

    private final EntityManager em;

    public ReservationRepository(EntityManager em) {
        this.em = em;
    }


    public List<Reservation> findAll() {
        String jpql = "SELECT r FROM Reservation r JOIN FETCH r.time JOIN FETCH r.theme WHERE r.deleted = false";

        return em.createQuery(jpql, Reservation.class)
                .getResultList();
    }

    public Reservation save(ReservationRequest reservationRequest, String reservationName) {
        Reservation reservation = new Reservation(
                reservationName,
                reservationRequest.getDate(),
                new Time(reservationRequest.getTime().toString()),
                new Theme(reservationName,reservationRequest.getTheme().toString())
        );
        em.persist(reservation);
        return reservation;
    }

    public void save(Reservation reservation) {
        em.persist(reservation);
    }

    public Optional<Reservation> findById(Long id) {
        return Optional.ofNullable(em.find(Reservation.class, id));
    }

    public void deleteById(Long id) {
        Reservation reservation = em.find(Reservation.class, id);
        if (reservation != null) {
            reservation.setDeleted(true); // 실제 삭제 대신 상태값 변경
        }
    }

    public List<Reservation> findByDateAndThemeId(String date, Long themeId) {
        return em.createQuery(
                        "SELECT r FROM Reservation r WHERE r.date = :date AND r.theme.id = :themeId AND r.deleted = false",
                        Reservation.class)
                .setParameter("date", date)
                .setParameter("themeId", themeId)
                .getResultList();
    }
}